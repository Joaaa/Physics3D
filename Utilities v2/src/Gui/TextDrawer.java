package Gui;

import java.awt.Color;
import java.util.Random;

import javax.swing.JOptionPane;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import Utilities.Display;
import Utilities.ErrorChecker;
import Utilities.Input;
import Utilities.VertexBuffer;
import Utilities.ShaderProgram;

public class TextDrawer {
	
	private Font font;
	private final ShaderProgram program = new ShaderProgram("text.vert", "text.frag");
	private final VertexBuffer buffer = new VertexBuffer();
	
	public TextDrawer(Font font){
		this.font = font;
		buffer.addAttribute("Position", 3);
		buffer.addAttribute("Uv", 2);
		for(int i = 0; i < 6; i++)
			buffer.addVertex();
	}
	
	public void setFont(Font font){
		this.font = font;
	}
	
	public void drawText(String text, int x, int y){
		program.use();
		boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
		if(!blend) GL11.glEnable(GL11.GL_BLEND);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		font.getTexture().bind();
		GL20.glUniform1i(program.getUniformLocation("text"), 0);
		Color c = font.getColor();
		GL20.glUniform3f(program.getUniformLocation("color"), c.getRed()/256f, c.getGreen()/256f, c.getBlue()/256f);
		float drawX = CoordTransform.getGlX(x);
		float drawY = CoordTransform.getGlY(y);
		for(int i = 0; i < text.length(); i++){
			int character = getCharAt(text, i);
			buffer.use();
			setupBuffer(drawX, drawY, character);
			GL20.glVertexAttribPointer(program.getLocation("vert"), 3, GL11.GL_FLOAT, false, 20, 0);
			GL20.glVertexAttribPointer(program.getLocation("uv"), 2, GL11.GL_FLOAT, false, 20, 12);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
			int nextChar = getCharAt(text, i+1);
			if(nextChar == -1){
				drawX += CoordTransform.getGlWidth(font.getCharWidth(character));
			}else{
				drawX += CoordTransform.getGlWidth(font.getCharWidth(character, nextChar));
			}
		}
		if(!blend) GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawTextCenteredAt(String text, int x, int y){
		drawText(text, x-getTextWidth(text)/2, y-getTextHeight(text)/2);
	}
	
	public int getTextWidth(String text){
		int width = 0;
		for(int i = 0; i < text.length(); i++){
			int character = getCharAt(text, i);
			int nextChar = getCharAt(text, i+1);
			if(nextChar == -1){
				width += font.getCharWidth(character);
			}else{
				width += font.getCharWidth(character, nextChar);
			}
		}
		return width;
	}
	
	public int getTextHeight(String text) {
		return font.getCharHeight();
	}
	
	private int getCharAt(String s, int pos){
		if(pos==s.length()) return -1;
		int character = s.charAt(pos);
		if(!font.supportsCharacter(character)){
			character = '?';
		}
		return character;
	}

	private void setupBuffer(float drawX, float drawY, int character) {
		float charWidth = CoordTransform.getGlWidth(font.getCharWidth(character));
		float charHeight = CoordTransform.getGlHeight(font.getCharHeight());
		float charXInTexture = font.getCharXInTexture(character);
		float charYInTexture = font.getCharYInTexture(character);
		float charWidthInTexture = font.getCharWidthInTexture(character);
		float charHeightInTexture = font.getCharHeightInTexture();
		buffer.setValues(0, "Position", drawX, drawY, 0);
		buffer.setValues(1, "Position", drawX, drawY+charHeight, 0);
		buffer.setValues(2, "Position", drawX+charWidth, drawY, 0);
		buffer.setValues(3, "Position", drawX+charWidth, drawY, 0);
		buffer.setValues(4, "Position", drawX, drawY+charHeight, 0);
		buffer.setValues(5, "Position", drawX+charWidth, drawY+charHeight, 0);
		buffer.setValues(0, "Uv", charXInTexture, charYInTexture);
		buffer.setValues(1, "Uv", charXInTexture, charYInTexture+charHeightInTexture);
		buffer.setValues(2, "Uv", charXInTexture+charWidthInTexture, charYInTexture);
		buffer.setValues(3, "Uv", charXInTexture+charWidthInTexture, charYInTexture);
		buffer.setValues(4, "Uv", charXInTexture, charYInTexture+charHeightInTexture);
		buffer.setValues(5, "Uv", charXInTexture+charWidthInTexture, charYInTexture+charHeightInTexture);
		buffer.flush();
	}
	
	public static int a = 1;

	public static void main(String[] args) {
		Display.instance.init(800, 600);
		
		Container c = new Container();
		SplitPane sp2 = new SplitPane(20, 20, 250, 100, 160, true);
		Container c2 = new Container(20, 20, 200, 200);
		sp2.setLeftElement(c2);
		sp2.setBorder(new Border(Color.BLACK));
		c2.setBackgroundColor(Color.BLUE);
		Label l = new Label(10, 30, "Test: "+a);
		l.setBorder(new Border(Color.GREEN));
		l.setFont(new Font("2", 32));
		Button b = new Button(20, 100, "Button");
		b.addClickListener(() -> {a*=2;l.setText("Test: "+a);});
		b.addClickListener(() -> System.out.println("Click"));
		c2.add(l);
		c2.add(b);
		c.add(sp2);
		
		SplitPane splitPane = new SplitPane(0, 0, 100, 500, 300, true);
		Container right = new Container();
		splitPane.setLeftElement(right);
		splitPane.setRightElement(c);
		splitPane.setBorder(new Border(Color.BLACK));
		right.add(new Button(10, 10, "Test"));
		while(!Display.instance.isClosed()){
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glClearColor(1, 1, 1, 1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			c.draw();
			splitPane.draw();
			Display.instance.update();
			ErrorChecker.check();
		}
	}

}
