package collision;

import rendering.WorldObject;

public class CollisionSphere extends CollisionModel{

    private final float radius;

    public CollisionSphere(WorldObject worldObject, float radius) {
        super(worldObject);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public CollisionResult checkCollision(CollisionModel other) {
        return other.checkCollisionWith(this);
    }

    @Override
    public CollisionResult checkCollisionWith(CollisionMesh mesh) {
        return CollisionChecker.checkMeshSphereCollision(mesh, this);
    }

    @Override
    public CollisionResult checkCollisionWith(CollisionSphere sphere) {
        return CollisionChecker.checkSphereSphereCollision(this, sphere);

    }

}
