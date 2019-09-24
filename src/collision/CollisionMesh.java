package collision;

import rendering.WorldObject;

public class CollisionMesh extends CollisionModel {

    public CollisionMesh(WorldObject worldObject) {
        super(worldObject);
    }

    @Override
    public CollisionResult checkCollision(CollisionModel other) {
        return other.checkCollisionWith(this);
    }

    @Override
    public CollisionResult checkCollisionWith(CollisionMesh mesh) {
        return CollisionChecker.checkMeshMeshCollision(this, mesh);
    }

    @Override
    public CollisionResult checkCollisionWith(CollisionSphere sphere) {
        return CollisionChecker.checkMeshSphereCollision(this, sphere);
    }

}
