package Utilities;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ShaderProgram {
	
	private static int currentProgram = -1;
	private int name, vertexAttribs;
	private Map<String, Integer> locations;
	private Map<String, Integer> uniformLocations;
	
	public ShaderProgram(String vert, String frag){
		name = Utilities.createShaderProgramme(new int[]{GL20.GL_VERTEX_SHADER, GL20.GL_FRAGMENT_SHADER}, new String[]{vert, frag});
		
		locations = new HashMap<String, Integer>();
		uniformLocations = new HashMap<String, Integer>();
		vertexAttribs = GL30.glGenVertexArrays();
	}
	
	public int getLocation(String param){
		Integer location = locations.get(param);
		if(location == null){
			location = GL20.glGetAttribLocation(name, param);
			if(location < 0){
				System.err.println("Error, something went wrong while getting location \""+param+"\"");
			}
			locations.put(param, location);
			GL30.glBindVertexArray(vertexAttribs);
			GL20.glEnableVertexAttribArray(location);
		}
		return location;
	}
	
	public int getUniformLocation(String param){
		Integer location = uniformLocations.get(param);
		if(location == null){
			location = GL20.glGetUniformLocation(name, param);
			if(location < 0){
				System.err.println("Error, something went wrong while getting uniform location \""+param+"\"");
			}
			uniformLocations.put(param, location);
		}
		return location;
	}
	
	public void use(){
		//if(currentProgram == name) return;
		GL20.glUseProgram(name);
		currentProgram = name;
		GL30.glBindVertexArray(vertexAttribs);
	}
	
}
