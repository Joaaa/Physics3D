package Utilities;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;

public class ErrorChecker {
	
	public static void check() {
		int error = GL11.glGetError();
		if(error != GL11.GL_NO_ERROR){
			System.err.println("Error "+error);
		}
	}
	
}
