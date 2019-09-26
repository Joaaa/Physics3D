package Gui;

public class Label extends GuiElement{
	
	private String text;
	private Font font;
	private TextDrawer textDrawer;
	private int padding = 2;
	
	public Label(int x, int y, String text){
		this.text = text;
		setRelativeX(x);
		setRelativeY(y);
		font = new Font("2", 16);
		textDrawer = new TextDrawer(font);
		updateSize();
	}
	
	private void updateSize(){
		setWidth(textDrawer.getTextWidth(text)+2*padding);
		setHeight(textDrawer.getTextHeight(text)+2*padding);
	}
	
	protected void draw() {
		super.draw();
		textDrawer.drawText(text, getAbsoluteX()+padding, getAbsoluteY()+padding);
	}
	
	public void setFont(Font f){
		font = f;
		textDrawer.setFont(f);
		updateSize();
	}
	
	public void setText(String text){
		this.text = text;
		updateSize();
	}
	
}
