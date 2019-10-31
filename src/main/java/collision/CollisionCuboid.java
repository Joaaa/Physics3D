package collision;

import Utilities.Vector4f;
import rendering.WorldObject;

public class CollisionCuboid extends CollisionModel {

    private final float xSize, ySize, zSize;
    private final float boundingSphereRadius;

    public CollisionCuboid(WorldObject worldObject, float size) {
        this(worldObject, size, size, size);
    }

    public CollisionCuboid(WorldObject worldObject, float xSize, float ySize, float zSize) {
        super(worldObject);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.boundingSphereRadius = new Vector4f(xSize, ySize, zSize, 0).getLength();
    }

    @Override
    public CollisionResult checkCollision(CollisionModel other) {
        return other.checkCollisionWith(this);
    }

    @Override
    public CollisionResult checkCollisionWith(CollisionCuboid cuboid) {
        return CollisionChecker.checkCuboidCuboidCollision(this, cuboid);
    }

    public Cuboid getTransformedCuboid() {
        return new Cuboid(getPosition().getLocation(), getPosition().getRotationMatrix(), xSize, ySize, zSize);
    }

    public float getBoundingSphereRadius() {
        return boundingSphereRadius;
    }

}
