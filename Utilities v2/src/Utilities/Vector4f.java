package Utilities;

public class Vector4f {
	
	public float x, y, z, w;
	
	public Vector4f(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f leftMult(Matrix4f m){
		return leftMult(m, true);
	}
	
	public Vector4f leftMult(Matrix4f m, boolean divw){
		Vector4f v = new Vector4f(0, 0, 0, 0);
		for(int r = 0; r < 4; r++){
			float value = 0;
			for(int e = 0; e < 4; e++){
				value += getValue(e) * m.getValue(r, e);
			}
			v.setValue(r, value);
		}
		return divw&&w!=0?v.multiply(1/v.w):v;
	}
	
	public Vector4f multiply(float f){
		return new Vector4f(x*f, y*f, z*f, w*f);
	}
	
	public float getValue(int i){
		return i==0?x:(i==1?y:(i==2?z:w));
	}
	
	public void setValue(int i, float value){
		if(i == 0)
			x = value;
		if(i == 1)
			y = value;
		if(i == 2)
			z = value;
		if(i == 3)
			w = value;
	}
	
	public Vector4f getInverted(){
		return new Vector4f(-getValue(0), -getValue(1), -getValue(2), -getValue(3));
	}
	
	@Override
	public String toString(){
		return "("+x+", "+y+", "+z+", "+w+")";
	}
	
	public float getLength(){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}

	public Vector4f normalize() {
		float l = getLength();
		Vector4f n = new Vector4f(x/l, y/l, z/l, w);
		return n;
	}
	
	public Vector4f add(Vector4f vec){
		return new Vector4f(x+vec.x, y+vec.y, z+vec.z, w+vec.w);
	}
	
	public static Vector4f crossProduct(Vector4f vec1, Vector4f vec2){
		return new Vector4f(vec1.y*vec2.z-vec1.z*vec2.y, vec1.z*vec2.x-vec1.x*vec2.z, vec1.x*vec2.y-vec1.y*vec2.x, vec1.w);
	}
	
	public float dotProduct(Vector4f vec){
		return x*vec.x+y*vec.y+z*vec.z;
	}
	
	public float distanceTo(Vector4f other){
		return (float) Math.sqrt(distanceTo(other));
	}
	
	public float distanceSqTo(Vector4f other){
		return (other.x-x)*(other.x-x)+(other.y-y)*(other.y-y)+(other.z-z)*(other.z-z);
	}
	
}
