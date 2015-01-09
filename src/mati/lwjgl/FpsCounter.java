package mati.lwjgl;

import org.lwjgl.glfw.GLFW;

/**
 * Created by Mateusz Paluch on 2015-01-09.
 * @author Mateusz Paluch
 */
public class FpsCounter {

	double lastFrameDuration;
	double lastUpdateTimestamp;
	double lastFpsUpdateTimestamp;
	int fps;
	int lastFrameCounter;
	int framesSinceLastUpdate;

	public double update() {
		framesSinceLastUpdate++;
		double now = GLFW.glfwGetTime();
		lastFrameDuration = now - lastUpdateTimestamp;
		lastUpdateTimestamp = now;
		if (now - lastFpsUpdateTimestamp > 1.0) {
			fps = framesSinceLastUpdate;
			lastFpsUpdateTimestamp = now;
			lastFrameCounter = framesSinceLastUpdate;
			framesSinceLastUpdate = 0;
		}
		return lastFrameDuration;
	}

	public int getFps() {
		return fps;
	}


}
