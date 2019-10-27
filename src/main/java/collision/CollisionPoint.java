package collision;

import Utilities.Vector4f;

public class CollisionPoint {

    private final Vector4f point;
    private final Vector4f normal;
    private final float collisionDepth;

    public CollisionPoint(Vector4f point, Vector4f normal, float CollisionDepth) {
        this.point = point;
        float s = normal.getLength();
        if(s < 0.0001) {
            this.normal = new Vector4f(1, 0, 0, 0);
        } else {
            this.normal = normal.multiply(1f/s);
        }
        collisionDepth = CollisionDepth;
    }

    public Vector4f getPoint() {
        return point;
    }

    public Vector4f getNormal() {
        return normal;
    }

    public float getCollisionDepth() {
        return collisionDepth;
    }

    @Override
    public String toString() {
        return "CollisionPoint: [p="+getPoint()+", n="+getNormal()+", d="+getCollisionDepth()+"]";
    }
}
