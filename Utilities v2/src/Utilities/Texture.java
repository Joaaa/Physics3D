package Utilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class Texture {
	
	private int name;
	
	public Texture(){
		name = GL11.glGenTextures();
	}
	
	public Texture(String fileName){
		this(fileName, true);
	}
	
	public Texture(String fileName, boolean linear){
		name = Utilities.loadTexture(fileName, linear);
	}
	
	public void bind(){
		bind(GL11.GL_TEXTURE_2D);
	}
	
	public void bind(int target){
		GL11.glBindTexture(target, name);
	}
	
	public void bindToLocation(int location, int unit){
		bindToLocation(location, unit, GL11.GL_TEXTURE_2D);
	}
	
	public void bindToLocation(int location, int unit, int target){
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(target, name);
		GL20.glUniform1i(location, unit);
	}
	
	public int getName(){
		return name;
	}
	
}
