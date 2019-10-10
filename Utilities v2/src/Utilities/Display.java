package Utilities;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Display {

	private GLFWErrorCallback errorCallback;
	private long window;
	private int width, height;
	public static Display instance = new Display();
	private boolean closed = false;
	
	private Display(){
		
	}
	
	public void initFullScreen(){
		init(-1, -1, true);
	}
	
	public void init(int w, int h){
		init(w, h, false);
	}
	
	private void init(int w, int h, boolean fullScreen) {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback
				.createPrint(System.err));
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE);
		glfwWindowHint(GLFW_SAMPLES, 8);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

		if(fullScreen){
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			width = vidmode.width();
			height = vidmode.height();
		}else{
			width = w;
			height = h;
		}
		
		window = glfwCreateWindow(width, height, "Title",
				fullScreen?GLFW.glfwGetPrimaryMonitor():MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		GL.setCapabilities(GL.createCapabilities());
		glfwShowWindow(window);

		GLFW.glfwSetKeyCallback(window, Input.getKeyCallback());
		GLFW.glfwSetCursorPosCallback(window, Input.getCursorCallback());
		GLFW.glfwSetMouseButtonCallback(window, Input.getMouseButtonCallback());
		GLFW.glfwSetScrollCallback(window, Input.getScrollCallback());
		
		GL11.glViewport(0, 0, width, height);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void close() {
		closed = true;
		GLFW.glfwSetWindowShouldClose(window, true);
	}
	
	public boolean isClosed(){
		return closed;
	}
	
	public void update(){
		if(closed){
			System.err.println("Error: window is closed. (Display.update())");
			errorCallback.close();
			Input.getKeyCallback().close();
			glfwTerminate();
		}
		IntBuffer w = BufferUtils.createIntBuffer(1), h = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(window, w, h);
		int newWidth = w.get();
		int newHeight = h.get();
		if(width != newWidth || height != newHeight){
			width = newWidth;
			height = newHeight;
			GL11.glViewport(0, 0, width, height);
		}
		if(GLFW.glfwWindowShouldClose(window)){
			closed = true;
			return;
		}
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public long getWindow(){
		return window;
	}

}
