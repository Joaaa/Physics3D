package collision;

import Utilities.Vector4f;

public class TriangleCollisionResult {

    private final Vector4f p1, p2, normal, middle;
    private final float length, depth;

    public TriangleCollisionResult(Vector4f p1, Vector4f p2, Vector4f normal, float depth) {
        this.p1 = p1;
        this.p2 = p2;
        this.length = p2.add(p1.getInverted()).getLength();
        this.normal = normal;
        this.depth = depth;
        this.middle = this.p1.add(this.p2).multiply(0.5f);
    }

    public Vector4f getP1() {
        return p1;
    }

    public Vector4f getP2() {
        return p2;
    }

    public Vector4f getNormal() {
        return normal;
    }

    public Vector4f getMiddle() {
        return middle;
    }

    public float getLength() {
        return length;
    }

    public float getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return "TriangleCollisionResult{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", normal=" + normal +
                ", middle=" + middle +
                ", length=" + length +
                ", depth=" + depth +
                '}';
    }

}
