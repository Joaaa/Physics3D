package collision;

import Utilities.Vector4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CollisionChecker {

    public static CollisionResult checkMeshMeshCollision(CollisionMesh mesh1, CollisionMesh mesh2) {
        List<TriangleCollisionResult> points = new ArrayList<>();
        for(MeshTriangle t1: mesh1.getTransformedFaces()) {
            for(MeshTriangle t2: mesh2.getTransformedFaces()) {
                TriangleCollisionResult result = t1.checkCollision(t2);
                if(result != null) {
                    points.add(result);
//                    System.out.println("Collision result:"+result);
                }
            }
        }
        if(points.stream().mapToDouble(TriangleCollisionResult::getLength).sum() < 0.0001f)
            return new CollisionResult(Collections.emptyList());
        float bestDepth = 0;
        Vector4f totalPoint = new Vector4f(0, 0, 0, 0);
        Vector4f totalNormal = new Vector4f(0, 0, 0, 0);
        float totalWeight = 0;
        for(TriangleCollisionResult point: points) {
            bestDepth = Math.max(bestDepth, point.getDepth());
            totalPoint = totalPoint.add(point.getMiddle().multiply(point.getLength()));
            totalNormal = totalNormal.add(point.getNormal().multiply(point.getLength()));
            totalWeight += point.getLength();
        }

        return new CollisionResult(
                Collections.singletonList(
                        new CollisionPoint(
                                totalPoint.multiply(1f/totalWeight),
                                totalNormal.multiply(1f/totalWeight),
                                bestDepth
                        )
                )
        );
//        return new CollisionResult(
//                Collections.singletonList(
//                        points.get(new Random().nextInt(points.size()))
//                )
//        );
//        return new CollisionResult(points);
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
