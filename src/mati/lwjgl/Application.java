package mati.lwjgl;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Created by Mateusz Paluch on 2015-01-09.
 * @author Mateusz Paluch
 */
public class Application  {

	private FpsCounter fpsCounter;
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private long window;
	private double targetFrameDuration = 1.0 / 60.0;

	public static void main(String[] args) {
		Application app = new Application();
		app.start();
	}

	public Application() {
		fpsCounter = new FpsCounter();
	}

	private void start() {
		init();
		play();
		dispose();
	}

	protected void init() {
		setProperties();
		setErrorCallback();
		initGLFW();
		createWindow();
		centerWindow();
		createOpenGLContext();
		enableVerticalSync();
		setKeyCallback();
	}

	protected void setProperties() {
		System.setProperty("org.lwjgl.librarypath", "C:\\Users\\Mateusz Paluch\\Projekty\\lwjgl-3\\native");
	}

	private void setErrorCallback() {
		errorCallback = Callbacks.errorCallbackPrint(System.err);
		GLFW.glfwSetErrorCallback(errorCallback);
	}

	private void initGLFW() {
		if (GLFW.glfwInit() != GL11.GL_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
	}

	private void createWindow() {
		window = GLFW.glfwCreateWindow(640, 480, "Simple example", MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			GLFW.glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}
	}

	private void centerWindow() {
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		int xpos = (GLFWvidmode.width(vidmode) - 640) / 2;
		int ypos = (GLFWvidmode.height(vidmode) - 480) / 2;
		GLFW.glfwSetWindowPos(window, xpos, ypos);
	}

	private void createOpenGLContext() {
		GLFW.glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
	}

	private void enableVerticalSync() {
		GLFW.glfwSwapInterval(60);
	}

	private void setKeyCallback() {
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
					GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
				}
			}
		};
		GLFW.glfwSetKeyCallback(window, keyCallback);
	}

	private void play() {
		while (isRunning()) {
			handleInput();
			updateLogic();
			render();

		}
	}

	private boolean isRunning() {
		return GLFW.glfwWindowShouldClose(window) != GL11.GL_TRUE;
	}

	private void dispose() {
		releaseWindow();
		terminateGLFW();
	}

	private void releaseWindow() {
		GLFW.glfwDestroyWindow(window);
		keyCallback.release();
	}

	private void terminateGLFW() {
		GLFW.glfwTerminate();
		errorCallback.release();
	}

	protected void handleInput() {
		GLFW.glfwPollEvents();
	}

	protected void updateLogic() {
		double delta = fpsCounter.update();
		int sleepTime = (int) ((targetFrameDuration - delta)*1000);
		System.out.println("delta=" + delta + " sleepTime=" + sleepTime + " fps=" + fpsCounter.getFps());
		if (sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void render() {
		GLFW.glfwSwapBuffers(window);
	}
}
