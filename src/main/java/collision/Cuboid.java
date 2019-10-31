package collision;

import Utilities.Matrix4f;
import Utilities.Vector4f;
import physics.Position;
import rendering.WorldObject;
import util.MathUtils;

import java.util.*;

public class Cuboid {

    private final float xSize, ySize, zSize;
    private final Vector4f center, xAxis, yAxis, zAxis;

    public Cuboid(Vector4f center, Matrix4f rotation, float xSize, float ySize, float zSize) {
        this.center = center;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        xAxis = new Vector4f(1, 0, 0, 0).leftMult(rotation);
        yAxis = new Vector4f(0, 1, 0, 0).leftMult(rotation);
        zAxis = new Vector4f(0, 0, 1, 0).leftMult(rotation);
    }

    public float getXSize() {
        return xSize;
    }

    public float getYSize() {
        return ySize;
    }

    public float getZSize() {
        return zSize;
    }

    public Vector4f getXAxis() {
        return xAxis;
    }

    public Vector4f getXVector() {
        return getXAxis().multiply(getXSize());
    }

    public Vector4f getYAxis() {
        return yAxis;
    }

    public Vector4f getYVector() {
        return getYAxis().multiply(getYSize());
    }

    public Vector4f getZAxis() {
        return zAxis;
    }

    public Vector4f getZVector() {
        return getZAxis().multiply(getZSize());
    }

    public Vector4f getCenter() {
        return center;
    }

    public List<Vector4f> getTransformedCorners() {
        Vector4f x = getXVector(), xi = x.getInverted();
        Vector4f y = getYVector(), yi = y.getInverted();
        Vector4f z = getZVector(), zi = z.getInverted();
        return Arrays.asList(
                center.add(x).add(y).add(z),
                center.add(x).add(y).add(zi),
                center.add(x).add(yi).add(zi),
                center.add(x).add(yi).add(z),
                center.add(xi).add(y).add(z),
                center.add(xi).add(y).add(zi),
                center.add(xi).add(yi).add(zi),
                center.add(xi).add(yi).add(z)
        );
    }

    public List<LineSegment> getTransformedEdges() {
        List<Vector4f> corners = getTransformedCorners();
        return Arrays.asList(
                new LineSegment(corners.get(0), corners.get(1)),
                new LineSegment(corners.get(1), corners.get(2)),
                new LineSegment(corners.get(2), corners.get(3)),
                new LineSegment(corners.get(3), corners.get(0)),
                new LineSegment(corners.get(0), corners.get(4)),
                new LineSegment(corners.get(1), corners.get(5)),
                new LineSegment(corners.get(2), corners.get(6)),
                new LineSegment(corners.get(3), corners.get(7)),
                new LineSegment(corners.get(4), corners.get(5)),
                new LineSegment(corners.get(5), corners.get(6)),
                new LineSegment(corners.get(6), corners.get(7)),
                new LineSegment(corners.get(7), corners.get(4))
        );
    }

    public List<CollisionPoint> checkCollision(LineSegment line) {
        Vector4f[] normals = {getXAxis(), getYAxis(), getZAxis()};
        float[] sizes = {getXSize(), getYSize(), getZSize()};

        float[] relCoordsStart = {
            line.getStart().dotProduct(normals[0])/sizes[0],
            line.getStart().dotProduct(normals[1])/sizes[1],
            line.getStart().dotProduct(normals[2])/sizes[2]
        };

        float[] relCoordsEnd = {
            line.getEnd().dotProduct(normals[0])/sizes[0],
            line.getEnd().dotProduct(normals[1])/sizes[1],
            line.getEnd().dotProduct(normals[2])/sizes[2]
        };

        for (int i = 0; i < 3; i++) {
            if((relCoordsStart[i] < -1 && relCoordsEnd[i] < -1) ||
                    ((relCoordsStart[i] > 1 && relCoordsEnd[i] > 1))) {
                return Collections.emptyList();
            }
        }

        List<CollisionPoint> result = new ArrayList<>();
        Vector4f startToEnd = line.getEnd().add(line.getStart().getInverted());

        for (int i = 0; i < 3; i++) {
            for(int sign = -1; sign < 2; sign += 2) {
                if ((relCoordsStart[i] - sign) * (relCoordsEnd[i] - sign) < 0) {
                    Vector4f intersection = line.getStart().add(startToEnd.multiply((sign - relCoordsStart[i]) / (relCoordsEnd[i] - relCoordsStart[i])));
                    boolean inPlane = true;
                    for (int j = 0; j < 3; j++) {
                        if (j != i && intersection.dotProduct(normals[j]) / sizes[j] > 1) {
                            inPlane = false;
                            break;
                        }
                    }
                    if(inPlane) {
                        float as = Math.abs(relCoordsStart[i]);
                        float ae = Math.abs(relCoordsEnd[i]);
                        result.add(new CollisionPoint(intersection, normals[i].multiply(sign), 1));
                    }
                }
            }
        }
        return result;
    }

}
