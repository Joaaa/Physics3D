package Gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import Utilities.VertexBuffer;
import Utilities.ShaderProgram;

public class Drawer {
	
private VertexBuffer buffer;
	
	private static ShaderProgram shader = new ShaderProgram("Drawer.vert", "Drawer.frag");
	private static VertexBuffer lineVB = new VertexBuffer();
	private static VertexBuffer quadVB = new VertexBuffer();

	static{
		lineVB.addAttribute("Position", 2);
		lineVB.addVertex();
		lineVB.addVertex();
		quadVB.addAttribute("Position", 2);
		quadVB.addVertex();
		quadVB.addVertex();
		quadVB.addVertex();
		quadVB.addVertex();
	}
	
	public static void drawLine(int x1, int y1, int x2, int y2, Color c){
		shader.use();
		lineVB.setValues(0, "Position", CoordTransform.getGlX(x1+0.5f), CoordTransform.getGlY(y1+0.5f));
		lineVB.setValues(1, "Position", CoordTransform.getGlX(x2+0.5f), CoordTransform.getGlY(y2+0.5f));
		lineVB.flush();
		lineVB.use();
		GL20.glVertexAttribPointer(shader.getLocation("pos"), 2, GL11.GL_FLOAT, false, 8, 0);
		GL20.glUniform3f(shader.getUniformLocation("color"), c.getRed()/256f, c.getGreen()/256f, c.getBlue()/256f);
		GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
	}
	
	public static void drawRect(int x1, int y1, int x2, int y2, Color c){
		shader.use();
		quadVB.setValues(0, "Position", CoordTransform.getGlX(x1+0.5f), CoordTransform.getGlY(y1+0.5f));
		quadVB.setValues(1, "Position", CoordTransform.getGlX(x2-0.5f), CoordTransform.getGlY(y1+0.5f));
		quadVB.setValues(2, "Position", CoordTransform.getGlX(x2-0.5f), CoordTransform.getGlY(y2-0.5f));
		quadVB.setValues(3, "Position", CoordTransform.getGlX(x1+0.5f), CoordTransform.getGlY(y2-0.5f));
		quadVB.flush();
		quadVB.use();
		GL20.glVertexAttribPointer(shader.getLocation("pos"), 2, GL11.GL_FLOAT, false, 8, 0);
		GL20.glUniform3f(shader.getUniformLocation("color"), c.getRed()/256f, c.getGreen()/256f, c.getBlue()/256f);
		GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 4);
	}
	
	public static void fillRect(int x1, int y1, int x2, int y2, Color c){
		shader.use();
		quadVB.setValues(0, "Position", CoordTransform.getGlX(x1), CoordTransform.getGlY(y1));
		quadVB.setValues(1, "Position", CoordTransform.getGlX(x2), CoordTransform.getGlY(y1));
		quadVB.setValues(2, "Position", CoordTransform.getGlX(x2), CoordTransform.getGlY(y2));
		quadVB.setValues(3, "Position", CoordTransform.getGlX(x1), CoordTransform.getGlY(y2));
		quadVB.flush();
		quadVB.use();
		GL20.glVertexAttribPointer(shader.getLocation("pos"), 2, GL11.GL_FLOAT, false, 8, 0);
		GL20.glUniform3f(shader.getUniformLocation("color"), c.getRed()/256f, c.getGreen()/256f, c.getBlue()/256f);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
	}
	
	public static Color getColorShifted(Color color, float shift){
		return new Color(color.getRed()/255f+shift, color.getGreen()/255f+shift, color.getBlue()/255f+shift);
	}
	
}
