package collision;

import Utilities.Vector4f;

public class CollisionPoint {

    private final Vector4f point;
    private final Vector4f normal;
    private final float collisionDepth;

    public CollisionPoint(Vector4f point, Vector4f normal, float CollisionDepth) {
        this.point = point;
        this.normal = normal.normalize();
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

}
