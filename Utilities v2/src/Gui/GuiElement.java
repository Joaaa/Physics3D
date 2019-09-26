package Gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.Action;

import org.lwjgl.glfw.GLFW;

import Utilities.Input;

public abstract class GuiElement {
	
	private int relX, relY, width, height;
	private Border border;
	private GuiElement container;
	private Color backgroundColor;
	private ArrayList<Runnable> clickEvents = new ArrayList<Runnable>(0);
	private boolean listensForClicks = false;
	
	public GuiElement(){
		this(0, 0, 0, 0);
	}
	
	public GuiElement(int x, int y, int width, int height){
		this.setRelativeX(x);
		this.setRelativeY(y);
		this.setWidth(width);
		this.setHeight(height);
	}

	public int getRelativeX() {
		return relX;
	}

	public void setRelativeX(int relX) {
		this.relX = relX;
	}
	
	public int getRelativeY() {
		return relY;
	}
	
	public int getAbsoluteX(){
		if(container == null)
			return getRelativeX();
		return getRelativeX() + container.getAbsoluteX();
	}
	
	public int getAbsoluteY(){
		if(container == null)
			return getRelativeY();
		return getRelativeY() + container.getAbsoluteY();
	}

	public void setRelativeY(int relY) {
		this.relY = relY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setBorder(Border border){
		this.border = border;
	}
	
	public void setBackgroundColor(Color color){
		backgroundColor = color;
	}
	
	public Color getBackgroundColor(){
		return backgroundColor;
	}
	
	protected void draw(){
		if(getBackgroundColor() != null)
			Drawer.fillRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX()+getWidth(), getAbsoluteY()+getHeight(), getBackgroundColor());
		if(border != null)
			border.draw(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
	}
	
	public void setContainer(GuiElement containter){
		this.container = containter;
	}
	
	public boolean isMouseOnElement(){
		return isOnElement((int)Input.getMouseX(), (int)Input.getMouseY());
	}
	
	public boolean isOnElement(int x, int y){
		return x >= getAbsoluteX() &&
				x < getAbsoluteX()+getWidth() &&
				y >= getAbsoluteY() &&
				y <= getAbsoluteY()+getHeight();
	}
	
	public void addClickListener(Runnable listener){
		clickEvents.add(listener);
		if(!listensForClicks){
			Input.addMouseReleaseConsumer(this::mouseReleaseListener);
			listensForClicks = true;
		}
	}
	
	private void mouseReleaseListener(int button){
		if(button == GLFW.GLFW_MOUSE_BUTTON_1 && isMouseOnElement()){
			clickEvents.forEach(r -> r.run());
		}
	}
	
}
