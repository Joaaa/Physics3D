package Gui;

import java.util.ArrayList;

public class Container extends GuiElement{
	
	private ArrayList<GuiElement> elements = new ArrayList<GuiElement>();

	public Container(){
		super();
	}
	
	public Container(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void add(GuiElement element){
		elements.add(element);
		element.setContainer(this);
	}

	public void draw() {
		super.draw();
		elements.forEach(e -> e.draw());
	}
	
}
