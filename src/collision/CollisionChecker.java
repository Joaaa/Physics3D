package collision;

import Utilities.Vector4f;

import java.util.Collections;

public class CollisionChecker {

    public static CollisionResult checkMeshMeshCollision(CollisionMesh mesh2, CollisionMesh mesh1) {
//        System.out.println("IMesh-mesh collision");
        return CollisionResult.EMPTY;
    }

    public static CollisionResult checkMeshSphereCollision(CollisionMesh mesh, CollisionSphere sphere) {
//        System.out.println("IMesh-sphere collision");
        return CollisionResult.EMPTY;
    }

    public static CollisionResult checkSphereSphereCollision(CollisionSphere sphere2, CollisionSphere sphere1) {
//        System.out.println("Sphere-sphere collision");
        Vector4f diffVec = sphere2.getPosition().getLocation().add(sphere1.getPosition().getLocation().getInverted());
        float distance = diffVec.getLength();
        float depth = (sphere1.getRadius() + sphere2.getRadius() - distance) / 2;
        if(depth < 0)
            return CollisionResult.EMPTY;

        Vector4f point = sphere1.getPosition().getLocation().add(diffVec.multiply((sphere1.getRadius()-depth)/distance));
        return new CollisionResult(Collections.singletonList(new CollisionPoint(point, diffVec, depth)));
    }

}
