package Gui;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import Utilities.Input;

public class Button extends GuiElement {
	
	private String text;
	private Font font;
	private TextDrawer textDrawer;
	
	public Button(int x, int y, String text){
		super(x, y, 0, 0);
		this.text = text;
		font = new Font("2", 20);
		textDrawer = new TextDrawer(font);
		setWidth(60+textDrawer.getTextWidth(text));
		setHeight(20+textDrawer.getTextHeight(text));
		setBackgroundColor(Color.LIGHT_GRAY);
		setBorder(new Border(Color.GRAY));
	}
	
	public Button(int x, int y, int width, int height, String text){
		super(x, y, width, height);
		this.text = text;
		font = new Font("2", 20);
		textDrawer = new TextDrawer(font);
		setBackgroundColor(Color.LIGHT_GRAY);
		setBorder(new Border(Color.GRAY));
	}
	
	@Override
	public Color getBackgroundColor() {
		if(super.getBackgroundColor() == null)
			return null;
		else
			return isMouseOnElement()?Drawer.getColorShifted(super.getBackgroundColor(), 0.05f):super.getBackgroundColor();
	}
	
	protected void draw() {
		super.draw();
		if(getBackgroundColor() != null){
			int lightEffectSize = (int) Math.min(getWidth()*0.05f, getHeight()*0.05f);
			Color color1 = isMouseOnElement()&&Input.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)?Drawer.getColorShifted(getBackgroundColor(), -0.1f):Drawer.getColorShifted(getBackgroundColor(), 0.05f);
			Color color2 = isMouseOnElement()&&Input.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)?Drawer.getColorShifted(getBackgroundColor(), 0.05f):Drawer.getColorShifted(getBackgroundColor(), -0.1f);
			Drawer.fillRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX()+getWidth(), getAbsoluteY()+lightEffectSize, color1);
			Drawer.fillRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX()+lightEffectSize, getAbsoluteY()+getHeight(), color1);
			Drawer.fillRect(getAbsoluteX(), getAbsoluteY()+(getHeight()-lightEffectSize), getAbsoluteX()+getWidth(), getAbsoluteY()+getHeight(), color2);
			Drawer.fillRect(getAbsoluteX()+(getWidth()-lightEffectSize), getAbsoluteY(), getAbsoluteX()+getWidth(), getAbsoluteY()+getHeight(), color2);
		}
		textDrawer.drawTextCenteredAt(text, getAbsoluteX()+getWidth()/2, getAbsoluteY()+getHeight()/2);
	}
	
}
