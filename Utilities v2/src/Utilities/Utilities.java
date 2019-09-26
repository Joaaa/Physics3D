package Utilities;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;


public class Utilities {
	
	private static final int fullScreenBuffer = GL15.glGenBuffers();
	private static final VertexBuffer fullScreenVertexBuffer;
	static{
		FloatBuffer fb = BufferUtils.createFloatBuffer(12);
		fb.put(new float[]{-1, 1, 1, -1, 1, 1, -1, -1, 1, -1, -1, 1});
		fb.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, fullScreenBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
		fullScreenVertexBuffer = new VertexBuffer();
		fullScreenVertexBuffer.addAttribute("Position", 3);
		fullScreenVertexBuffer.addAttribute("Normal", 3);
		fullScreenVertexBuffer.addAttribute("Uv", 2);
		fullScreenVertexBuffer.addQuad(new Vector4f(-1f, -1f, 0, 1), new Vector4f(1f, -1f, 0, 1), new Vector4f(-1f, 1f, 0, 1), new Vector4f(1f, 1f, 0, 1));
		fullScreenVertexBuffer.setValues(0, "Uv", 0, 0);
		fullScreenVertexBuffer.setValues(1, "Uv", 1, 0);
		fullScreenVertexBuffer.setValues(2, "Uv", 0, 1);
		fullScreenVertexBuffer.setValues(3, "Uv", 0, 1);
		fullScreenVertexBuffer.setValues(4, "Uv", 1, 0);
		fullScreenVertexBuffer.setValues(5, "Uv", 1, 1);
	}
	
	public static int getFullScreenBuffer(){
		return fullScreenBuffer;
	}
	
	public static int createShaderProgramme(int[] shaderTypes, String[] shaderSource){
		int[] shaders = new int[shaderTypes.length];
		for(int i = 0; i < shaderTypes.length; i++){
			String shaderCode = "";
			Scanner s = null;
			try{
				s = new Scanner(new File(shaderSource[i]));
			}catch(IOException e){
				s = new Scanner(getResource(shaderSource[i]));
			}
			while(s.hasNext()){
				shaderCode += s.nextLine()+"\n";
			}
			s.close();
			shaders[i] = CreateShader(shaderTypes[i], shaderCode);
		}
		return createShaderProgramme(shaders);
	}
	
	private static int CreateShader(int shaderType, String shaderString) {
		int shader = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(shader, shaderString);
		GL20.glCompileShader(shader);
		int status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
		if (status != GL11.GL_TRUE)
			System.err.println(GL20.glGetShaderInfoLog(shader));
		return shader;
	}
	
	private static int createShaderProgramme(int[] shaders) {
		int program = GL20.glCreateProgram();
		for (int i = 0; i < shaders.length; i++) {
			GL20.glAttachShader(program, shaders[i]);
		}
		GL20.glLinkProgram(program);
		int status = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS);
		if (status != GL11.GL_TRUE) {
			String error = GL20.glGetProgramInfoLog(program);
			System.err.println(error);
		}
		return program;
	}
	
	public static void loadObj(String name, List<Float> buffer){
		List<Float> vertices = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		List<Float> uvs = new ArrayList<Float>();
		
		Scanner scan = null;
		try{
			scan = new Scanner(new File(name));
		}catch(IOException e){
			scan = new Scanner(getResource(name));
		}
		while(scan.hasNext()){
			String line = scan.nextLine().replace("  ", " ");
			String[] words = line.split(" ");
			if(words[0].equals("v")){
				vertices.add(Float.parseFloat(words[1]));
				vertices.add(Float.parseFloat(words[2]));
				vertices.add(Float.parseFloat(words[3]));
			}else if(words[0].equals("vn")){
				normals.add(Float.parseFloat(words[1]));
				normals.add(Float.parseFloat(words[2]));
				normals.add(Float.parseFloat(words[3]));
			}else if(words[0].equals("vt")){
				uvs.add(Float.parseFloat(words[1]));
				uvs.add(Float.parseFloat(words[2]));
			}else if(words[0].equals("f")){
				for(int i = 1; i < 4; i++){
					String[] ind = words[i].split("/");
					for(int j = 0; j < 3; j++){
						buffer.add(vertices.get((Integer.parseInt(ind[0])-1)*3+j));
					}
					for(int j = 0; j < 3; j++){
						buffer.add(normals.get((Integer.parseInt(ind[2])-1)*3+j));
					}
					if(ind[1].equals("")){
						buffer.add(0f);
						buffer.add(0f);
					}else{
						buffer.add(uvs.get((Integer.parseInt(ind[1])-1)*2));
						buffer.add(1-uvs.get((Integer.parseInt(ind[1])-1)*2+1));
					}
				}
			}
		}
		scan.close();
		
	}
	
	public static void loadObjectToBuffer(String name, VertexBuffer buffer){
		List<Float> list = new ArrayList<Float>();
		loadObj(name, list);
		for(int i = 0; i < list.size()/8; i++){
			buffer.setValues(i, "Position", list.get(i*8), list.get(i*8+1), list.get(i*8+2));
			buffer.setValues(i, "Normal", list.get(i*8+3), list.get(i*8+4), list.get(i*8+5));
			buffer.setValues(i, "Uv", list.get(i*8+6), list.get(i*8+7));
		}
	}
	
	static int loadTexture(String name, boolean linear){
		try {
			BufferedImage b = null;
			try{
				b = ImageIO.read(new File(name));
			}catch(IOException e){
				b = ImageIO.read(getResource(name));
			}
			int textureID = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			ByteBuffer buffer = BufferUtils.createByteBuffer(b.getWidth()*b.getHeight()*3);
			for(int i = 0; i < b.getWidth()*b.getHeight(); i++){
				int color = b.getRGB(i%b.getWidth(), i/b.getWidth());
				buffer.put((byte)((color & 0x00ff0000) >> 16));
				buffer.put((byte)((color & 0x0000ff00) >> 8));
				buffer.put((byte)((color & 0x000000ff)));
			}
			buffer.flip();
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, b.getWidth(), b.getHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, linear?GL11.GL_LINEAR:GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, linear?GL11.GL_LINEAR_MIPMAP_LINEAR:GL11.GL_NEAREST_MIPMAP_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			return textureID;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private static int textureBuffer = GL15.glGenBuffers();
	public static void drawImageToTexture(BufferedImage image, Texture texture){
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth()*image.getHeight()*4);
		int wth = image.getWidth()*image.getHeight();
		int[] colors = new int[wth];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), colors, 0, image.getWidth());
		for(int i = 0; i < wth; ++i){
			int color = colors[i];
			buffer.put((byte)((color & 0x00ff0000) >> 16));
			buffer.put((byte)((color & 0x0000ff00) >> 8));
			buffer.put((byte)((color & 0x000000ff)));
			buffer.put((byte)0);
		}
		buffer.flip();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, textureBuffer);
		GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
		texture.bind(GL31.GL_TEXTURE_BUFFER);
		GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL11.GL_RGBA8, textureBuffer);
	}
	
	public static InputStream getResource(String resource){
		if(!resource.startsWith("/"))
			resource = "/"+resource;
		return Utilities.class.getResourceAsStream(resource);
	}
	
}
