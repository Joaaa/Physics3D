package Utilities;
import org.lwjgl.opengl.*;

public class ShadowMap {
	
	public static ShaderProgram shadowShader = new ShaderProgram("Shadow.vert", "Shadow.frag");
	private int frameBuffer, depthBuffer, quality;
	private Texture texture;
	
	public ShadowMap(int quality){
		this.quality = quality;
		frameBuffer = GL30.glGenFramebuffers();
		depthBuffer = GL30.glGenRenderbuffers();
		texture = new Texture();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		texture.bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_R16, quality, quality, 0, GL11.GL_RED, GL11.GL_FLOAT, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture.getName(), 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, quality, quality);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
	}
	
	public void start(){
		shadowShader.use();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, quality, quality);
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public Texture getTexture(){
		return texture;
	}
	
}
