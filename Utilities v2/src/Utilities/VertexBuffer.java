package Utilities;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class VertexBuffer {
	
	private int name;
	private ArrayList<Float> values;
	private HashMap<String, int[]> attributes;
	private int dataAmount = 0;
	private int vertexAmount = 0;
	private int bufferedVertexAmount = 0;
	
	public VertexBuffer(){
		name = GL15.glGenBuffers();
		values = new ArrayList<Float>();
		attributes = new HashMap<String, int[]>();
	}
	
	public void addAttribute(String name, int size){
		attributes.put(name, new int[]{dataAmount, size});
		dataAmount += size;
	}
	
	public void addQuad(Vector4f p1, Vector4f p2, Vector4f p3, Vector4f p4){
		if(dataAmount < 6){
			System.err.println("Error, VertexBuffer size too small.");
		}
		Vector4f normal = Vector4f.crossProduct(p2.add(p1.getInverted()), p3.add(p1.getInverted())).normalize();
		addVertex(p1, normal);
		addVertex(p2, normal);
		addVertex(p3, normal);
		normal = Vector4f.crossProduct(p2.add(p3.getInverted()), p4.add(p3.getInverted())).normalize();
		addVertex(p3, normal);
		addVertex(p2, normal);
		addVertex(p4, normal);
	}
	
	public void addQuad(Vector4f p1, Vector4f p2, Vector4f p3, Vector4f p4, Vector4f color){
		if(dataAmount < 9){
			System.err.println("Error, VertexBuffer size too small.");
		}
		Vector4f normal = Vector4f.crossProduct(p2.add(p1.getInverted()), p3.add(p1.getInverted())).normalize();
		addVertex(p1, color, normal);
		addVertex(p2, color, normal);
		addVertex(p3, color, normal);
		normal = Vector4f.crossProduct(p2.add(p3.getInverted()), p4.add(p3.getInverted())).normalize();
		addVertex(p3, color, normal);
		addVertex(p2, color, normal);
		addVertex(p4, color, normal);
	}
	
	private void addVertex(Vector4f position, Vector4f normal){
		values.add(position.x);
		values.add(position.y);
		values.add(position.z);
		values.add(normal.x);
		values.add(normal.y);
		values.add(normal.z);
		for(int i = 6; i < dataAmount; i++){
			values.add(-1f);
		}
		vertexAmount++;
	}
	
	private void addVertex(Vector4f position, Vector4f color, Vector4f normal){
		values.add(position.x);
		values.add(position.y);
		values.add(position.z);
		values.add(color.x);
		values.add(color.y);
		values.add(color.z);
		values.add(normal.x);
		values.add(normal.y);
		values.add(normal.z);
		for(int i = 9; i < dataAmount; i++){
			values.add(-1f);
		}
		vertexAmount++;
	}
	
	public void addVertex(){
		for(int i = 0; i < dataAmount; i++){
			values.add(-1f);
		}
		vertexAmount++;
	}
	
	public void setValues(int vertex, String attribName, float... values){
		if(!attributes.containsKey(attribName)){
			System.err.println("Error, attribute "+attribName+" not found.");
			return;
		}
		int[] attribInfo = attributes.get(attribName);
		if(values.length != attribInfo[1]){
			System.err.println("Error, incorrect value amount.");
			return;
		}
		while(vertex >= vertexAmount){
			addVertex();
		}
		for(int i = 0; i < values.length; i++){
			this.values.set(vertex*dataAmount+attribInfo[0]+i, values[i]);
		}
	}
	
	public void flush(){
		FloatBuffer b = BufferUtils.createFloatBuffer(values.size());
		for(float f: values){
			b.put(f);
		}
		b.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, name);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, b, GL15.GL_STATIC_DRAW);
		bufferedVertexAmount = vertexAmount;
	}
	
	public void use(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, name);
	}
	
	public int getVertexAmount(){
		return bufferedVertexAmount;
	}
	
	public void vertexAttribPointer(int location, String attribName){
		if(location < 0) return;
		if(!attributes.containsKey(attribName)){
			System.err.println("Error, attribute "+attribName+" not found.");
			return;
		}
		int[] attribInfo = attributes.get(attribName);
		GL20.glVertexAttribPointer(location, attribInfo[1], GL11.GL_FLOAT, false, dataAmount*4, attribInfo[0]*4);
	}
	
	public ArrayList<Float> getValues(){
		return values;
	}
	
	public float[] getValues(String attribName){
		int[] attrib = attributes.get(attribName);
		float[] val = new float[attrib[1]*vertexAmount];
		for(int i = 0; i < vertexAmount; i++){
			for(int j = 0; j < attrib[1]; j++){
				val[i*attrib[1]+j] = values.get(i*dataAmount+attrib[0]+j);
			}
		}
		return val;
	}
	
}
