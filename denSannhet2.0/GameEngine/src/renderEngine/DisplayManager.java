package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	// creating local variables for the screen size, and update rate of the screen.
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 760;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	/**
	 *  Creating the display, where the graphical content will be appearing.
	 */
	public static void createDisplay(){
		// decides what open GL version we are using, current 4.4
		ContextAttribs attribs = new ContextAttribs(4,4)
		.withForwardCompatible(true)
		.withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Assignment 1");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		// Telling open GL the size of the display
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	};
	
	
	public static void updateDisplay(){		
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	};
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay(){		
		Display.destroy();
	};
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	
}
