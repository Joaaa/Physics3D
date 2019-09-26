package collision;

import Utilities.Matrix4f;
import Utilities.Vector4f;

public class MeshTriangle {

    private final Vector4f p1, p2, p3;
    private final Vector4f v12, v13;
    private final Vector4f normal;

    public MeshTriangle(Vector4f p1, Vector4f p2, Vector4f p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;


        this.v12 = p2.add(p1.getInverted());
        this.v13 = p3.add(p1.getInverted());
        this.normal = Vector4f.crossProduct(v12, v13).normalize();
    }

    public Vector4f getP1() {
        return p1;
    }

    public Vector4f getP2() {
        return p2;
    }

    public Vector4f getP3() {
        return p3;
    }

    public Vector4f getNormal() {
        return normal;
    }

    public MeshTriangle transform(Matrix4f transform) {
        return new MeshTriangle(p1.leftMult(transform), p2.leftMult(transform), p3.leftMult(transform));
    }

    public CollisionPoint checkCollision(Vector4f centerOfMass, Vector4f corner) {
//        System.out.println(this+", "+centerOfMass+", "+corner);
        float distance = centerOfMass.add(p1.getInverted()).dotProduct(normal);
//        System.out.println(distance);
        if(distance < 0)
            return null;

        float depth = normal.dotProduct(p1.add(corner.getInverted()));
        if(depth < 0)
            return null;

        Vector4f projected = corner.add(normal.multiply(normal.dotProduct(p1.add(corner.getInverted()))));
//        System.out.println(projected);

        Vector4f p = projected.add(p1.getInverted());
        Vector4f q = Vector4f.crossProduct(v12, normal);
        float u = p.dotProduct(q)/v13.dotProduct(q);
//        System.out.println(u);
        if(u < 0 || u > 1)
            return null;
        float v = p.add(v13.multiply(u).getInverted()).dotProduct(v12)/(v12.x*v12.x+v12.y*v12.y+v12.z*v12.z);
//        System.out.println(v);
        if(v < 0 ||  v+u > 1)
            return null;
        return new CollisionPoint(corner, normal, depth);
    }

    @Override
    public String toString() {
        return "Triangle: [p1="+p1+", p2="+p2+", p3="+p3+"]";
    }
}
