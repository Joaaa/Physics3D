package Gui;

import Utilities.Display;

public class CoordTransform {
	
	private static final float GLXMIN = -1, GLXMAX = 1, GLYMIN = 1, GLYMAX = -1;
	private static final float SCREENXMIN = 0f, SCREENYMIN = 0;
	
	public static int getScreenX(float glX){
		return (int) mapCoord(glX, GLXMIN, GLXMAX, SCREENXMIN, Display.instance.getWidth()-0.5f);
	}
	
	public static int getScreenY(float glY){
		return (int) mapCoord(glY, GLYMIN, GLYMAX, SCREENYMIN, Display.instance.getHeight()-0.5f);
	}
	
	public static float getGlX(float screenX){
		return mapCoord(screenX, SCREENXMIN, Display.instance.getWidth(), GLXMIN, GLXMAX);
	}
	
	public static float getGlY(float screenY){
		return mapCoord(screenY, SCREENYMIN, Display.instance.getHeight(), GLYMIN, GLYMAX);
	}
	
	public static int getScreenWidth(float glWidth){
		return (int) mapSize(glWidth, GLXMIN, GLXMAX, SCREENXMIN, Display.instance.getWidth());
	}
	
	public static int getScreenHeight(float glHeight){
		return (int) mapSize(glHeight, GLYMIN, GLYMAX, SCREENYMIN, Display.instance.getHeight());
	}
	
	public static float getGlWidth(float screenWidth){
		return mapSize(screenWidth, SCREENXMIN, Display.instance.getWidth(), GLXMIN, GLXMAX);
	}
	
	public static float getGlHeight(float screenHeight){
		return mapSize(screenHeight, SCREENYMIN, Display.instance.getHeight(), GLYMIN, GLYMAX);
	}
	
	private static float mapCoord(float number, float min1, float max1, float min2, float max2){
		return (number-min1)/(max1-min1)*(max2-min2) + min2;
	}
	
	private static float mapSize(float number, float min1, float max1, float min2, float max2){
		return (number)/(max1-min1)*(max2-min2);
	}
	
}
