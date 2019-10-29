package Utilities;

public class Camera {
	
	private Vector4f position, rotation;
	private Matrix4f viewMatrix, projectionMatrix, VP, inverse;
	
	public Camera(){
		position = new Vector4f(0, 0, 0, 0);
		rotation = new Vector4f(0, 0, 0, 0);
		updateViewMatrix();
		setProjection(true, Display.instance.getWidth(), Display.instance.getHeight(), 0.1f, 100);
	}
	
	public void setPosition(Vector4f pos){
		position = pos;
		updateViewMatrix();
	}
	
	public Vector4f getPosition(){
		return position;
	}
	
	public void setRotation(Vector4f rot){
		rotation = rot;
		updateViewMatrix();
	}
	
	public Vector4f getRotation(){
		return rotation;
	}
	
	public void rotate(Vector4f rot){
		rotation = rotation.add(rot);
		updateViewMatrix();
	}
	
	public void setViewMatrix(Matrix4f mat){
		viewMatrix = mat;
		updateVP();
	}
	
	private void updateViewMatrix(){
		viewMatrix = Matrix4f.getViewMatrix(position, rotation.y, rotation.x, rotation.z);
		updateVP();
	}
	
	public void setProjection(boolean perspective, float width, float height, float near, float far){
		if(perspective){
			projectionMatrix = Matrix4f.getProjectionMatrix(width, height, near, far, 1.2f);
		}else{
			projectionMatrix = Matrix4f.getOrthoProjectionMatrix(width, height, near, far);
		}
		updateVP();
	}
	
	public void setProjection(Matrix4f p){
		projectionMatrix = p;
		updateVP();
	}
	
	private void updateVP(){
		if(viewMatrix == null || projectionMatrix == null) return;
		VP = viewMatrix.leftMult(projectionMatrix);
		inverse = VP.getInverted();
	}
	
	public Matrix4f getViewMatrix(){
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public Matrix4f getVPMatrix(){
		return VP;
	}
	
	public Matrix4f getInverseMatrix(){
		return inverse;
	}
	
}
