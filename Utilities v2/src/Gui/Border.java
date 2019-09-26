package Gui;

import java.awt.Color;

public class Border {
	
	private Color color;
	
	public Border(Color color){
		this.color = color;
	}
	
	public void draw(int x, int y, int width, int height){
		Drawer.drawRect(x-1, y-1,  x+width+1, y+height+1, color);
	}
	
}
