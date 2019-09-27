package collision;

import Utilities.Vector4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollisionChecker {

    public static CollisionResult checkMeshMeshCollision(CollisionMesh mesh1, CollisionMesh mesh2) {
//        return new CollisionResult(Collections.emptyList());
        List<CollisionPoint> points = new ArrayList<>();
        for(MeshTriangle t1: mesh1.getTransformedFaces()) {
            for(MeshTriangle t2: mesh2.getTransformedFaces()) {
                CollisionPoint p = t1.checkCollision(t2);
                if(p != null) {
                    System.out.println(t1);
                    System.out.println(t2);
                    System.out.println(p);
                    points.add(p);
                }
            }
        }

        return new CollisionResult(points);
    }

//    private static List<CollisionPoint> checkHalfMeshCollision(CollisionMesh mesh1, CollisionMesh mesh2) {
//        List<CollisionPoint> points = new ArrayList<>();
//        for(Vector4f corner: mesh1.getTransformedCorners()) {
//            CollisionPoint leastDeepCollision = null;
//            for(MeshTriangle triangle: mesh2.getTransformedFaces()) {
//                CollisionPoint result = triangle.checkCollision(mesh1.getPosition().getLocation(), corner);
//                if(result != null && (leastDeepCollision == null || result.getCollisionDepth() < leastDeepCollision.getCollisionDepth()))
//                    leastDeepCollision = result;
//            }
//            if(leastDeepCollision != null)
//                points.add(leastDeepCollision);
//        }
//        return points;
//    }

    public static CollisionResult checkMeshSphereCollision(CollisionMesh mesh, CollisionSphere sphere) {
//        System.out.println("IMesh-sphere collision");
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
