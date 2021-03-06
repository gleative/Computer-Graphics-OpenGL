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
	
	// will hold the time of the end of the last frame.
	private static long lastFrameTime;
	
	// holds onto the time taken to render previous frame.
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
	
	/**
	 * Sync, updates and keep track of the time of the game.
	 */
	public static void updateDisplay(){		
		Display.sync(FPS_CAP);
		Display.update();
		
		// gets the current time
		long currentFrameTime = getCurrentTime();
		
		// subtract the current time with last time, we end up with the difference. We divide it so we get the 
		// answer in to seconds.
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		
		// Updates the variable, before going on next loop.
		lastFrameTime = currentFrameTime;
	};
	
	/**
	 *  Simple getter to let other class access the time
	 * @return delta - a float representing time.
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay(){		
		Display.destroy();
	};
	
	/**
	 *  Sys.getTime will return the time in ticks, by dividing it with Sys.getTimerResolution 
	 *  we end up answere in seconds. Since we want milliseconds instead of seconds, we multiply what we get from
	 *  Sys.getTime by 1000.
	 * @return float - returns time in milliseconds.
	 */
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	
}
