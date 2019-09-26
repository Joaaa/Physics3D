package Gui;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import Utilities.Input;

public class SplitPane extends GuiElement{
	
	private GuiElement left, right;
	private int leftWidth, rightWidth;
	private boolean moveableCenter;
	private int centerWidth = 12;
	
	public SplitPane(int x, int y, int leftWidth, int rightWidth, int height, boolean moveableCenter){
		super(x, y, leftWidth+10+rightWidth, height);
		this.leftWidth = leftWidth;
		this.rightWidth = rightWidth;
		this.moveableCenter = moveableCenter;
		if(moveableCenter)
			Input.addMouseMovementConsumer(this::mouseMoved);
	}
	
	private void mouseMoved(float x, float y){
		if(Input.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)){
			if(Input.getMouseX() >= getAbsoluteX()+leftWidth &&
					Input.getMouseX() < getAbsoluteX()+leftWidth+centerWidth &&
					Input.getMouseY() >= getAbsoluteY() &&
					Input.getMouseY() < getAbsoluteY()+getHeight()){
				leftWidth += x;
				rightWidth -= x;
				if(leftWidth < 10){
					rightWidth -= 10-leftWidth;
					leftWidth = 10;
				}else if(rightWidth < 10){
					leftWidth -= 10-rightWidth;
					rightWidth = 10;
				}
				updateDimensions();
			}
		}
	}
	
	public void setLeftElement(GuiElement element){
		left = element;
		left.setContainer(this);
		updateDimensions();
	}
	
	public void setRightElement(GuiElement element){
		right = element;
		right.setContainer(this);
		updateDimensions();
	}
	
	private void updateDimensions(){
		setWidth(leftWidth+10+rightWidth);
		if(left != null){
			left.setRelativeX(0);
			left.setRelativeY(0);
			left.setWidth(leftWidth);
			left.setHeight(getHeight());
		}
		if(right != null){
			right.setRelativeX(leftWidth+centerWidth);
			right.setRelativeY(0);
			right.setWidth(rightWidth);
			right.setHeight(getHeight());
		}
	}
	
	public void draw() {
		super.draw();
		if(left != null)
			left.draw();
		if(right != null)
			right.draw();
		Color centerColor = moveableCenter&&isMouseOnElement()&&Input.getMouseX()>=leftWidth&&Input.getMouseX()<leftWidth+centerWidth?Drawer.getColorShifted(Color.LIGHT_GRAY, 0.05f):Color.LIGHT_GRAY;
		Color color1 = Drawer.getColorShifted(centerColor, 0.1f);
		Color color2 = Drawer.getColorShifted(centerColor, -0.1f);
		Drawer.fillRect(getAbsoluteX()+leftWidth, getAbsoluteY(), getAbsoluteX()+leftWidth+centerWidth, getAbsoluteY()+getHeight(), centerColor);
		Drawer.fillRect(getAbsoluteX()+leftWidth, getAbsoluteY(), getAbsoluteX()+leftWidth+centerWidth, getAbsoluteY()+1, color1);
		Drawer.fillRect(getAbsoluteX()+leftWidth, getAbsoluteY(), getAbsoluteX()+leftWidth+1, getAbsoluteY()+getHeight(), color1);
		Drawer.fillRect(getAbsoluteX()+leftWidth, getAbsoluteY()+getHeight()-1, getAbsoluteX()+leftWidth+centerWidth, getAbsoluteY()+getHeight(), color2);
		Drawer.fillRect(getAbsoluteX()+leftWidth+centerWidth-1, getAbsoluteY(), getAbsoluteX()+leftWidth+centerWidth, getAbsoluteY()+getHeight(), color2);
		if(moveableCenter){
			int gripLinesStart = leftWidth+centerWidth/2-3;
			Drawer.drawLine(getAbsoluteX()+gripLinesStart, getAbsoluteY()+getHeight()/2-7, getAbsoluteX()+gripLinesStart, getAbsoluteY()+getHeight()/2+7, color1);
			Drawer.drawLine(getAbsoluteX()+gripLinesStart+1, getAbsoluteY()+getHeight()/2-7, getAbsoluteX()+gripLinesStart+1, getAbsoluteY()+getHeight()/2+7, color2);
			Drawer.drawLine(getAbsoluteX()+gripLinesStart+2, getAbsoluteY()+getHeight()/2-8, getAbsoluteX()+gripLinesStart+2, getAbsoluteY()+getHeight()/2+8, color1);
			Drawer.drawLine(getAbsoluteX()+gripLinesStart+3, getAbsoluteY()+getHeight()/2-8, getAbsoluteX()+gripLinesStart+3, getAbsoluteY()+getHeight()/2+8, color2);
			Drawer.drawLine(getAbsoluteX()+gripLinesStart+4, getAbsoluteY()+getHeight()/2-7, getAbsoluteX()+gripLinesStart+4, getAbsoluteY()+getHeight()/2+7, color1);
			Drawer.drawLine(getAbsoluteX()+gripLinesStart+5, getAbsoluteY()+getHeight()/2-7, getAbsoluteX()+gripLinesStart+5, getAbsoluteY()+getHeight()/2+7, color2);
		}
		//Drawer.drawRect(getAbsoluteX()+leftWidth, getAbsoluteY(), getAbsoluteX()+leftWidth+centerWidth, getAbsoluteY()+getHeight(), Color.GRAY);
	}
	
}
