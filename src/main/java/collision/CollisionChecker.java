package collision;

import Utilities.Vector4f;
import util.MathUtils;

import java.util.*;
import java.util.stream.Stream;

public class CollisionChecker {

    public static CollisionResult checkCuboidCuboidCollision(CollisionCuboid cm1, CollisionCuboid cm2) {
        if(cm1.getPosition().getLocation().add(cm2.getPosition().getLocation().getInverted()).getLength()
                > cm1.getBoundingSphereRadius()+cm2.getBoundingSphereRadius())
            return CollisionResult.EMPTY;
        Cuboid cuboid1 = cm1.getTransformedCuboid();
        Cuboid cuboid2 = cm2.getTransformedCuboid();

        List<CollisionPoint> result = new ArrayList<>(checkCorners(cuboid1, cuboid2));
        result.addAll(checkCorners(cuboid2, cuboid1));
        if(result.isEmpty()) {
            for (LineSegment edge : cuboid2.getTransformedEdges())
                result.addAll(cuboid1.checkCollision(edge));
            for (LineSegment edge : cuboid1.getTransformedEdges())
                result.addAll(cuboid2.checkCollision(edge));
        }
        return new CollisionResult(result);
    }

    private static List<CollisionPoint> checkCorners(Cuboid cuboid1, Cuboid cuboid2) {
        List<CollisionPoint> collisions = new ArrayList<>();

        Vector4f[] normals = {cuboid1.getXAxis(), cuboid1.getYAxis(), cuboid1.getZAxis()};
        float[] sizes = {cuboid1.getXSize(), cuboid1.getYSize(), cuboid1.getZSize()};

        for(Vector4f point: cuboid2.getTransformedCorners()) {
            Vector4f p = point.add(cuboid1.getCenter().getInverted());

            float[] relCoords = {
                    p.dotProduct(normals[0])/sizes[0],
                    p.dotProduct(normals[1])/sizes[1],
                    p.dotProduct(normals[2])/sizes[2]
            };
            float[] relCoordsAbs = MathUtils.abs(relCoords);
            if(!MathUtils.allLessThan(relCoordsAbs, 1))
                continue;

            int pushAxis = MathUtils.argMax(relCoordsAbs);
            Vector4f normal = normals[pushAxis];
            if(relCoords[pushAxis] < 0)
                normal = normal.getInverted();

            float depth = (1-relCoordsAbs[pushAxis])*sizes[pushAxis];

            collisions.add(new CollisionPoint(p, normal, depth));
        }
        return collisions;
    }

    public static CollisionResult checkMeshMeshCollision(CollisionMesh mesh1, CollisionMesh mesh2) {
        if(mesh1.getPosition().getLocation().add(mesh2.getPosition().getLocation().getInverted()).getLength()
                > mesh1.getBoundingSphereRadius()+mesh2.getBoundingSphereRadius())
            return CollisionResult.EMPTY;

        List<CollisionPoint> points = new ArrayList<>();
        for(MeshTriangle t1: mesh1.getTransformedFaces()) {
            float distToCenter = t1.getNormal().dotProduct(mesh2.getPosition().getLocation().add(t1.getP1().getInverted()));
            if(Math.abs(distToCenter) > mesh2.getBoundingSphereRadius())
                continue;
            for(MeshTriangle t2: mesh2.getTransformedFaces()) {
                CollisionPoint result = t1.checkCollision(t2);
                if(result != null) {
                    points.add(result);
//                    System.out.println("Collision result:"+result);
                }
            }
        }

        return new CollisionResult(points);

//        if(points.stream().mapToDouble(TriangleCollisionResult::getLength).sum() < 0.0001f)
//            return new CollisionResult(Collections.emptyList());
//
//        Vector4f totalPoint = new Vector4f(0, 0, 0, 0);
//        Vector4f totalNormal = new Vector4f(0, 0, 0, 0);
//        float totalWeight = 0;
//        for(TriangleCollisionResult point: points) {
//            totalPoint = totalPoint.add(point.getMiddle().multiply(point.getLength()));
//            totalNormal = totalNormal.add(point.getNormal().multiply(point.getLength()));
//            totalWeight += point.getLength();
//        }
//
//        return new CollisionResult(
//                Collections.singletonList(
//                        new CollisionPoint(
//                                totalPoint.multiply(1f/totalWeight),
//                                totalNormal.multiply(1f/totalWeight),
//                                totalWeight/100f
//                        )
//                )
//        );
    }

    public static CollisionResult checkMeshSphereCollision(CollisionMesh mesh, CollisionSphere sphere) {
        return CollisionResult.EMPTY;
    }

    public static CollisionResult checkSphereSphereCollision(CollisionSphere sphere1, CollisionSphere sphere2) {
        Vector4f diffVec = sphere2.getPosition().getLocation().add(sphere1.getPosition().getLocation().getInverted());
        float distance = diffVec.getLength();
        float depth = (sphere1.getRadius() + sphere2.getRadius() - distance) / 2;
        if(depth < 0)
            return CollisionResult.EMPTY;

        Vector4f point = sphere1.getPosition().getLocation().add(diffVec.multiply((sphere1.getRadius()-depth)/distance));
        return new CollisionResult(Collections.singletonList(new CollisionPoint(point, diffVec, depth)));
    }

}
