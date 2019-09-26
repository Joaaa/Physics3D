package Utilities;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public class TextureDrawer {
	
	private static ShaderProgram shader = new ShaderProgram("Texture.vert", "Texture.frag");
	private static final int shader2 = Utilities.createShaderProgramme(new int[]{GL_VERTEX_SHADER, GL_FRAGMENT_SHADER}, new String[]{"Texture2.vert", "Texture2.frag"});
	private static final int buffer = GL15.glGenBuffers();
	private static final int buffer2 = GL15.glGenBuffers();
	private static final int[] locations2 = {GL20.glGetAttribLocation(shader2, "pos"), GL20.glGetAttribLocation(shader2, "coord"), GL20.glGetUniformLocation(shader2, "text"), GL20.glGetUniformLocation(shader2, "width"), GL20.glGetUniformLocation(shader2, "height")};
	static{
		FloatBuffer fb = BufferUtils.createFloatBuffer(12);
		fb.put(new float[]{-1, 1, 1, -1, 1, 1, -1, -1, 1, -1, -1, 1});
		fb.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
		fb = BufferUtils.createFloatBuffer(24);
		fb.put(new float[]{-1, 1, 0, 1, 1, -1, 1, 0, 1, 1, 1, 1, -1, -1, 0, 0, 1, -1, 1, 0, -1, 1, 0, 1});
		fb.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer2);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
	}
	
	public static void drawTexture(Texture texture, int width, int height){
		shader.use();
		GL11.glViewport(0, 0, width, height);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL20.glVertexAttribPointer(shader.getLocation("pos"), 2, GL11.GL_FLOAT, false, 8, 0);
		texture.bindToLocation(shader.getUniformLocation("text"), 0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
	}
	
	public static void drawBufferTexture(Texture texture, int width, int height){
		GL20.glUseProgram(shader2);
		GL11.glViewport(0, 0, width, height);
		GL20.glEnableVertexAttribArray(locations2[0]);
		GL20.glEnableVertexAttribArray(locations2[1]);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer2);
		GL20.glVertexAttribPointer(locations2[0], 2, GL11.GL_FLOAT, false, 16, 0);
		GL20.glVertexAttribPointer(locations2[1], 2, GL11.GL_FLOAT, false, 16, 8);
		texture.bindToLocation(locations2[2], 0, GL31.GL_TEXTURE_BUFFER);
		GL20.glUniform1i(locations2[3], width);
		GL20.glUniform1i(locations2[4], height);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
	}
	
}
