package Utilities;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;


public class Input {

	private static keyboardHandler keyCallback = new keyboardHandler();
	private static GLFWCursorPosCallback cursorCallback = new mouseHandler();
	private static GLFWMouseButtonCallback mouseButtonCallback = new mouseButtonHandler();
	private static GLFWScrollCallback scrollCallback = new scrollCallback();
	private static ArrayList<Consumer<Float>> scrollConsumers = new ArrayList<Consumer<Float>>();
	private static ArrayList<BiConsumer<Float, Float>> mouseMovementConsumers = new ArrayList<BiConsumer<Float, Float>>();
	private static ArrayList<Consumer<Integer>> keyReleaseConsumers = new ArrayList<Consumer<Integer>>();
	private static ArrayList<Consumer<Integer>> mouseReleaseConsumers = new ArrayList<Consumer<Integer>>();
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static float mouseX = 0, mouseY = 0;
	private static boolean mousePosCorrect = false;
	
	public static float getMouseX(){
		return mouseX;
	}
	
	public static float getMouseY(){
		return mouseY;
	}
	
	public static void addMouseMovementConsumer(BiConsumer<Float, Float> consumer){
		mouseMovementConsumers.add(consumer);
	}
	
	public static GLFWKeyCallback getKeyCallback(){
		return keyCallback;
	}
	
	public static void addKeyReleaseConsumer(Consumer<Integer> consumer){
		keyReleaseConsumers.add(consumer);
	}
	
	public static GLFWCursorPosCallback getCursorCallback(){
		return cursorCallback;
	}
	
	public static GLFWMouseButtonCallback getMouseButtonCallback(){
		return mouseButtonCallback;
	}
	
	public static boolean isKeyDown(int key){
		if(key >= keys.length) return false;
		return keys[key];
	}
	
	public static boolean isMouseButtonDown(int button){
		if(button >= buttons.length) return false;
		return buttons[button];
	}
	
	public static void addMouseReleaseConsumer(Consumer<Integer> consumer){
		mouseReleaseConsumers.add(consumer);
	}
	
	public static GLFWScrollCallback getScrollCallback(){
		return scrollCallback;
	}
	
	public static void addScrollConsumer(Consumer<Float> consumer){
		scrollConsumers.add(consumer);
	}
	
	static class keyboardHandler extends GLFWKeyCallback{
		
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if(key == GLFW.GLFW_KEY_ESCAPE)
				Display.instance.close();
			if(key < 0||key >= keys.length)
				return;
			if(action == GLFW.GLFW_PRESS)
				keys[key] = true;
			else if(action == GLFW.GLFW_RELEASE){
				keys[key] = false;
				keyReleaseConsumers.forEach(c -> c.accept(key));
			}
		}
		
	}
	
	static class mouseHandler extends GLFWCursorPosCallback{

		public void invoke(long window, double xpos, double ypos) {
			if(mousePosCorrect){
				mouseMovementConsumers.forEach(c->c.accept((float)xpos-mouseX, (float)ypos-mouseY));
			}else{
				mousePosCorrect = true;
			}
			mouseX = (float) xpos;
			mouseY = (float) ypos;
		}
		
	}
	
	static class mouseButtonHandler extends GLFWMouseButtonCallback{

		public void invoke(long window, int button, int action, int mods) {
			if(button < 0||button >= buttons.length)
				return;
			if(action == GLFW.GLFW_PRESS){
				buttons[button] = true;
			}else if(action == GLFW.GLFW_RELEASE){
				buttons[button] = false;
				mouseReleaseConsumers.forEach(c -> c.accept(button));
			}
		}
		
	}
	
	static class scrollCallback extends GLFWScrollCallback{

		public void invoke(long window, double xoffset, double yoffset) {
			scrollConsumers.forEach(s->s.accept((float)yoffset));
		}
		
	}

}
