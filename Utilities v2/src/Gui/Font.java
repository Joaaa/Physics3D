package Gui;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import Utilities.Input;
import Utilities.Texture;

public class Font {
	
	private final HashMap<Integer, FontCharacter> characters = new HashMap<Integer, FontCharacter>();
	private final HashMap<KerningPair, Integer> kernings = new HashMap<KerningPair, Integer>();
	private Texture texture;
	private int size;
	private Color color = Color.BLACK;
	
	public Font(String name){
		this(name, 32);
	}
	
	public Font(String name, int size){
		this.size = size;
		texture = new Texture(name+".png");
		Scanner scan = new Scanner(Font.class.getResourceAsStream("/"+name+".fnt"));
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			if(line.startsWith("char ")){
				characters.put(getValue(line, "id"),
						new FontCharacter(getValue(line, "x"), getValue(line, "y"), getValue(line, "width")));
			}else if(line.startsWith("kerning ")){
				kernings.put(new KerningPair(getValue(line, "first"), getValue(line, "second")), getValue(line, "amount"));
			}
		}
		scan.close();
	}
	
	private static int getValue(String s, String key){
		int beginIndex = s.indexOf(key+"=")+key.length()+1;
		int endIndex = s.indexOf(" ", beginIndex);
		if(endIndex == -1) endIndex = s.length();
		return Integer.parseInt(s.substring(beginIndex, endIndex));
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public boolean supportsCharacter(int character){
		return characters.containsKey(character);
	}
	
	public float getCharXInTexture(int character){
		return characters.get(character).getX()/512f;
	}
	
	public float getCharYInTexture(int character){
		return characters.get(character).getY()/512f;
	}
	
	public int getCharWidth(int character){
		return (int)Math.ceil(characters.get(character).getWidth()*size/32f);
	}
	
	public int getCharWidth(int character, int nextChar){
		float w = characters.get(character).getWidth();
		KerningPair key = new KerningPair(character, nextChar);
		if(kernings.containsKey(key)){
			w += kernings.get(key);
		}
		return (int)Math.ceil(w*size/32f);
	}
	
	public float getCharWidthInTexture(int character){
		return characters.get(character).getWidth()/512f;
	}
	
	public int getCharHeight(){
		return size;
	}
	
	public float getCharHeightInTexture(){
		return 32/512f;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	private class KerningPair{
		
		private int first, second;
		
		public KerningPair(int first, int second){
			this.first = first;
			this.second = second;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;
			if(!(obj instanceof KerningPair))
				return false;
			KerningPair other = (KerningPair) obj;
			return this.first == other.first &&
					this.second == other.second;
		}
		
		@Override
		public int hashCode() {
			return first*9973+second;
		}
		
	}
	
}
