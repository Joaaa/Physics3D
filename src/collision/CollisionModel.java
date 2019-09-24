package collision;

import physics.Position;
import rendering.WorldObject;

public abstract class CollisionModel {

    private final WorldObject worldObject;

    public CollisionModel(WorldObject worldObject) {
        this.worldObject = worldObject;
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }

    public Position getPosition() {
        return worldObject.getPosition();
    }

    public CollisionResult checkCollision(CollisionModel other) {
        return CollisionResult.EMPTY;
    }

    public CollisionResult checkCollisionWith(CollisionMesh mesh) {
        return CollisionResult.EMPTY;
    }

    public CollisionResult checkCollisionWith(CollisionSphere sphere) {
        return CollisionResult.EMPTY;
    }

}
