package collision;

import Utilities.Vector4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollisionChecker {

    public static CollisionResult checkMeshMeshCollision(CollisionMesh mesh1, CollisionMesh mesh2) {
        List<CollisionPoint> points = new ArrayList<>();
        for(MeshTriangle t1: mesh1.getTransformedFaces()) {
            for(MeshTriangle t2: mesh2.getTransformedFaces()) {
                CollisionPoint p = t1.checkCollision(t2);
                if(p != null) {
                    points.add(p);
                }
            }
        }

        return new CollisionResult(points);
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
