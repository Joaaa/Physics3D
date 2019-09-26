package Utilities;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Particle {
	
	private Vector4f position, speed, color;
	private float timeLeft, size;
	private boolean alive = true;
	private static final Vector4f ACCELERATION = new Vector4f(0, -0.981f, 0, 0);
	private static ShaderProgram shader = new ShaderProgram("Particle.vert", "Particle.frag");
	private static VertexBuffer vertexBuffer;
	private static Random random = new Random();
	private float orientation;
	static{
		vertexBuffer = new VertexBuffer();
		vertexBuffer.addAttribute("Position", 3);
		vertexBuffer.addAttribute("Normal", 3);
		vertexBuffer.addQuad(new Vector4f(-1, -1, 0, 0), new Vector4f(1, -1, 0, 0), new Vector4f(-1, 1, 0, 0), new Vector4f(1, 1, 0, 0));
		vertexBuffer.flush();
	}
	
	public Particle(Vector4f position, Vector4f speed, Vector4f color, float lifeSpan, float size){
		this.position = position;
		this.speed = speed;
		this.color = color;
		this.timeLeft = lifeSpan*(0.8f + 0.4f*random.nextFloat());
		this.size = size;
		this.orientation = random.nextFloat()*(float)Math.PI*2;
	}
	
	public void update(float dt){
		timeLeft -= dt;
		if(timeLeft <= 0){
			die();
		}
		position = position
				.add(speed.multiply(dt))
				.add(ACCELERATION.multiply(dt*dt/2));
		speed = speed
				.add(ACCELERATION.multiply(dt))
				.multiply(1-(0.10f*dt));
	}
	
	public void draw(Matrix4f VP, float rotY, float rotX){
		shader.use();
		vertexBuffer.use();
		vertexBuffer.vertexAttribPointer(shader.getLocation("pos"), "Position");
		Matrix4f MVP = Matrix4f.getScaleMatrix(new Vector4f(size, size, size, 1))
				.leftMult(Matrix4f.getRotationMatrix(new Vector4f(0, 0, 1, 0), orientation))
				.leftMult(Matrix4f.getRotationMatrix(new Vector4f(1, 0, 0, 0), -rotX))
				.leftMult(Matrix4f.getRotationMatrix(new Vector4f(0, 1, 0, 0), rotY))
				.leftMult(Matrix4f.getTranslationMatrix(position))
				.leftMult(VP);
		GL20.glUniformMatrix4fv(shader.getUniformLocation("MVP"), false, MVP.toFloatBuffer());
		GL20.glUniform4f(shader.getUniformLocation("color"), color.x, color.y, color.z, color.w);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexBuffer.getVertexAmount());
	}
	
	private void die(){
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public Vector4f getPosition(){
		return position;
	}
	
	public void setPosition(Vector4f position){
		this.position = position;
	}
	
	public Vector4f getSpeed(){
		return speed;
	}
	
	public void setSpeed(Vector4f speed){
		this.speed = speed;
	}
	
}
