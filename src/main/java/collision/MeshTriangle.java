package collision;

import Utilities.Matrix4f;
import Utilities.Vector4f;

import java.util.Arrays;
import java.util.List;

public class MeshTriangle {

    private final Vector4f p1, p2, p3;
    private final Vector4f v12, v13;
    private final Vector4f normal;
    private float planarDistance;

    public MeshTriangle(Vector4f p1, Vector4f p2, Vector4f p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        this.v12 = p2.add(p1.getInverted());
        this.v13 = p3.add(p1.getInverted());
        this.normal = Vector4f.crossProduct(v12, v13).normalize();

        planarDistance = -normal.dotProduct(p1);
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

    public List<Vector4f> getPoints() {
        return Arrays.asList(p1, p2, p3);
    }

    public Vector4f getNormal() {
        return normal;
    }

    public float getPlanarDistance() {
        return planarDistance;
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

    public CollisionPoint checkCollision(MeshTriangle other) {
        // Are planes parallel?
        if(Math.abs(getNormal().dotProduct(other.getNormal())) > 0.9999)
            return null;

        float d11 = other.getNormal().dotProduct(p1) + other.getPlanarDistance();
        float d12 = other.getNormal().dotProduct(p2) + other.getPlanarDistance();
        float d13 = other.getNormal().dotProduct(p3) + other.getPlanarDistance();
        float d21 = this.getNormal().dotProduct(other.p1) + this.getPlanarDistance();
        float d22 = this.getNormal().dotProduct(other.p2) + this.getPlanarDistance();
        float d23 = this.getNormal().dotProduct(other.p3) + this.getPlanarDistance();
//        System.out.println(Arrays.toString(new float[]{d11, d12, d13, d21, d22, d23}));

        // Early stop
        if(allSameSide(d11, d12, d13) || allSameSide(d21, d22, d23))
            return null;

        Vector4f O;
        if(Math.abs(other.getNormal().dotProduct(v12)) < 0.0001)
            O = p1.add(v13.multiply(-d11/v13.dotProduct(other.getNormal())));
        else
            O = p1.add(v12.multiply(-d11/v12.dotProduct(other.getNormal())));

        Vector4f D = Vector4f.crossProduct(this.getNormal(), other.getNormal()).normalize();
        Vector4f Oinv = O.getInverted();

        float p11 = D.dotProduct(this.p1.add(Oinv));
        float p12 = D.dotProduct(this.p2.add(Oinv));
        float p13 = D.dotProduct(this.p3.add(Oinv));
        float p21 = D.dotProduct(other.p1.add(Oinv));
        float p22 = D.dotProduct(other.p2.add(Oinv));
        float p23 = D.dotProduct(other.p3.add(Oinv));

        float t11 = p11+(p12-p11)*d11/(d11-d12);
        float t12 = p12+(p13-p12)*d12/(d12-d13);
        float t13 = p13+(p11-p13)*d13/(d13-d11);
        float t21 = p21+(p22-p21)*d21/(d21-d22);
        float t22 = p22+(p23-p22)*d22/(d22-d23);
        float t23 = p23+(p21-p23)*d23/(d23-d21);
//        System.out.println(Arrays.toString(new float[]{t11, t12, t13, t21, t22, t23}));

        float min1 = minFinite(t11, t12, t13);
        float max1 = maxFinite(t11, t12, t13);
        float min2 = minFinite(t21, t22, t23);
        float max2 = maxFinite(t21, t22, t23);
//        System.out.println(Arrays.toString(new float[]{min1, max1, min2, max2}));

        if(min1 > max2 || min2 > max1)
            return null;

        float t1 = Math.max(min1, min2);
        float t2 = Math.min(max1, max2);
        float t = (t1+t2)/2f;
        Vector4f intersectionMiddle = O.add(D.multiply(t));

        float minDistT1 = -largestNegative(d11, d12, d13);
        float minDistT2 = -largestNegative(d21, d22, d23);

        Vector4f n = this.normal;
        if(minDistT1 < minDistT2)
            n = other.normal;

        return new CollisionPoint(intersectionMiddle, n, Math.min(minDistT1, minDistT2));
    }

    private float minFinite(float t1, float t2, float t3) {
        float min = t1;
        if(Float.isFinite(t2) && (Float.isInfinite(min) || t2 < min))
            min = t2;
        if(Float.isFinite(t3) && (Float.isInfinite(min) || t3 < min))
            min = t3;
        return min;
    }

    private float maxFinite(float t1, float t2, float t3) {
        float max = t1;
        if(Float.isFinite(t2) && (Float.isInfinite(max) || t2 > max))
            max = t2;
        if(Float.isFinite(t3) && (Float.isInfinite(max) || t3 > max))
            max = t3;
        return max;
    }

    private boolean allSameSide(float d1, float d2, float d3) {
        return (d1 > 0 && d2 > 0 && d3 > 0) || (d1 < 0 && d2 < 0 && d3 < 0);
    }

    private float largestNegative(float d1, float d2, float d3) {
        float largest = Float.NEGATIVE_INFINITY;
        if(d1 < 0)
            largest = d1;
        if(d2 < 0 && d2 > largest)
            largest = d2;
        if(d3 < 0 && d3 > largest)
            largest = d3;
        return largest;
    }

    @Override
    public String toString() {
        return "Triangle: [p1="+p1+", p2="+p2+", p3="+p3+"]";
    }
}
