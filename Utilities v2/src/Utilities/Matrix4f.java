package Utilities;
import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;


public class Matrix4f{
	
	private float[] mfa;
	
	public Matrix4f(float[] m){
		if(m.length != 16)
			new Exception("Matrix float array size doesn't fit.").printStackTrace();
		mfa = m.clone();
	}
	
	public static Matrix4f getEmptyMatrix(){
		float[] f = new float[16];
		Arrays.fill(f, 0);
		return new Matrix4f(f);
	}
	
	public static Matrix4f getIdentityMatrix(){
		return new Matrix4f(new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1});
	}
	
	public static Matrix4f getRotationMatrix(Vector4f axis, float angle){
		Vector4f norm = axis.normalize();
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		Matrix4f rotated = getEmptyMatrix();
		rotated.setValue(0, 0, cos + norm.getValue(0)*norm.getValue(0)*(1-cos));
		rotated.setValue(1, 0, norm.getValue(2)*sin + norm.getValue(1)*norm.getValue(0)*(1-cos));
		rotated.setValue(2, 0, -norm.getValue(1)*sin + norm.getValue(2)*norm.getValue(0)*(1-cos));
		rotated.setValue(0, 1, -norm.getValue(2)*sin + norm.getValue(0)*norm.getValue(1)*(1-cos));
		rotated.setValue(1, 1, cos + norm.getValue(1)*norm.getValue(1)*(1-cos));
		rotated.setValue(2, 1, norm.getValue(0)*sin + norm.getValue(2)*norm.getValue(1)*(1-cos));
		rotated.setValue(0, 2, norm.getValue(1)*sin + norm.getValue(0)*norm.getValue(2)*(1-cos));
		rotated.setValue(1, 2, -norm.getValue(0)*sin + norm.getValue(1)*norm.getValue(2)*(1-cos));
		rotated.setValue(2, 2, cos + norm.getValue(2)*norm.getValue(2)*(1-cos));
		rotated.setValue(3, 3, 1);
		return rotated;
	}
	
	public static Matrix4f getTranslationMatrix(Vector4f translation){
		return new Matrix4f(new float[]{
			1, 0, 0, translation.x,
			0, 1, 0, translation.y,
			0, 0, 1, translation.z,
			0, 0, 0, 1
			});
	}
	
	public static Matrix4f getScaleMatrix(Vector4f scale){
		Matrix4f rotated = getEmptyMatrix();
		for(int i = 0; i < 3; i++)
			rotated.setValue(i, i, scale.getValue(i));
		rotated.setValue(3, 3, 1);
		return rotated;
	}
	
	public static Matrix4f getViewMatrix(Vector4f pos, float rotY, float rotX){
		return getTranslationMatrix(pos.getInverted()).leftMult(getRotationMatrix(new Vector4f(0, 1, 0, 0), -rotY)).leftMult(getRotationMatrix(new Vector4f(1, 0, 0, 0), -rotX));
	}
	
	public static Matrix4f getViewMatrix(Vector4f pos, float rotY, float rotX, float rotZ){
		return getTranslationMatrix(pos.getInverted()).leftMult(getRotationMatrix(new Vector4f(0, 1, 0, 0), -rotY)).leftMult(getRotationMatrix(new Vector4f(1, 0, 0, 0), -rotX)).leftMult(getRotationMatrix(new Vector4f(0, 0, 1, 0), -rotZ));
	}
		
	public static Matrix4f getProjectionMatrix(float width, float height, float near, float far){
		return new Matrix4f(new float[]{2*height/width, 0, 0, 0, 0, 2, 0, 0, 0, 0, -(far+near)/(far-near), -2*far*near/(far-near), 0, 0, -1, 0});
	}
	
	public static Matrix4f getProjectionMatrix(float width, float height, float near, float far, float fov){
		return new Matrix4f(new float[]{1/((float)Math.tan(fov/2) * width/height), 0, 0, 0, 0, 1/(float)Math.tan(fov/2), 0, 0, 0, 0, -(far+near)/(far-near), -2*far*near/(far-near), 0, 0, -1, 0});
	}
	
	public static Matrix4f getProjectionMatrixFov(float fovH, float fovV, float near, float far){
		return new Matrix4f(new float[]{1/((float)Math.tan(fovH/2)), 0, 0, 0, 0, 1/((float)Math.tan(fovV/2)), 0, 0, 0, 0, -(far+near)/(far-near), -2*far*near/(far-near), 0, 0, -1, 0});
	}
	
	public static Matrix4f getOrthoProjectionMatrix(float width, float height, float near, float far){
		return new Matrix4f(new float[]{2/width, 0, 0, 0, 0, 2/height, 0, 0, 0, 0, -2/(far-near), -(far+near)/(far-near), 0, 0, 0, 1});
	}
	
	public Matrix4f getInverted(){
		Matrix4f inverted = getEmptyMatrix();
		float invdet = 1/getDeterminant();
		for(int r = 0; r < 4; r++){
			for(int c = 0; c < 4; c++){
				inverted.setValue(r, c, invdet*((r+c)%2==0?1:-1)*getMinor(c, r));
			}
		}
		return inverted;
	}
	
	public float getDeterminant(){
		return getValue(0, 0)*getMinor(0, 0) -
				getValue(1, 0)*getMinor(1, 0) +
				getValue(2, 0)*getMinor(2, 0) -
				getValue(3, 0)*getMinor(3, 0);
	}
	
	public float getDeterminant3(){
		return getValue(0, 0)*(getValue(1, 1)*getValue(2, 2)-getValue(1, 2)*getValue(2, 1)) -
				getValue(1, 0)*(getValue(0, 1)*getValue(2, 2)-getValue(0, 2)*getValue(2, 1)) +
				getValue(2, 0)*(getValue(0, 1)*getValue(1, 2)-getValue(0, 2)*getValue(1, 1));
	}
	
	public float getMinor(int row, int column){
		Matrix4f minor = getEmptyMatrix();
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				minor.setValue(i, j, getValue((i>=row?1:0) + i, (j>=column?1:0) + j));
			}
		}
		return minor.getDeterminant3();
	}
	
	public FloatBuffer toFloatBuffer(){
		float[] fba = new float[16];
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				fba[i*4+j] = mfa[i+4*j];
			}
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(16).put(fba).flip();
	}
	
	public Matrix4f leftMult(Matrix4f m){
		Matrix4f product = getEmptyMatrix();
		for(int c = 0; c < 4; c++){
			for(int r = 0; r < 4; r++){
				float value = 0;
				for(int e =  0; e < 4; e++){
					value += getValue(e, c) * m.getValue(r, e);
				}
				product.setValue(r, c, value);
			}
		}
		return product;
	}
	
	public float getValue(int row, int column){
		return mfa[row*4+column];
	}
	
	public void setValue(int row, int column, float value){
		mfa[row*4+column] = value;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				s += mfa[4*i+j] + " ";
			}
			s += "\n";
		}
		return s;
	}
	
}
