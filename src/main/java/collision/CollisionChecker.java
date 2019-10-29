package collision;

import Utilities.Vector4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CollisionChecker {

    public static CollisionResult checkMeshMeshCollision(CollisionMesh mesh1, CollisionMesh mesh2) {
        if(mesh1.getPosition().getLocation().add(mesh2.getPosition().getLocation().getInverted()).getLength()
                > mesh1.getBoundingSphereRadius()+mesh2.getBoundingSphereRadius())
            return CollisionResult.EMPTY;

        List<TriangleCollisionResult> points = new ArrayList<>();
        for(MeshTriangle t1: mesh1.getTransformedFaces()) {
            float distToCenter = t1.getNormal().dotProduct(mesh2.getPosition().getLocation().add(t1.getP1().getInverted()));
            if(Math.abs(distToCenter) > mesh2.getBoundingSphereRadius())
                continue;
            for(MeshTriangle t2: mesh2.getTransformedFaces()) {
                TriangleCollisionResult result = t1.checkCollision(t2);
                if(result != null) {
                    points.add(result);
//                    System.out.println("Collision result:"+result);
                }
            }
        }

        return new CollisionResult(
                points.stream().map(p -> new CollisionPoint(p.getMiddle(), p.getNormal(), p.getLength())).collect(Collectors.toList())
        );

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
