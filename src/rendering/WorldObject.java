package rendering;

import Utilities.Vector4f;
import collision.CollisionModel;
import collision.CollisionSphere;
import physics.*;

import java.util.Arrays;

public class WorldObject {

    private CollisionModel collisionModel;
    private PhysicsState physicsState;

    public WorldObject() {
        this.physicsState = new PhysicsStateImmovable(this, new Position());
    }

    public void setCollisionModel(CollisionModel collisionModel) {
        this.collisionModel = collisionModel;
    }

    public void setPhysicsState(PhysicsState physicsState) {
        this.physicsState = physicsState;
    }

    /**
     * Return the collision model, or null if none is present.
     * @return
     */
    public CollisionModel getCollisionModel() {
        return collisionModel;
    }

    public PhysicsState getPhysicsState() {
        return physicsState;
    }

    public static void main(String[] args) {
        WorldObject o1 = new WorldObject();
        o1.setPhysicsState(new PhysicsStateImmovable(o1, new Position()));
        o1.setCollisionModel(new CollisionSphere(o1, 0.3f));
        WorldObject o2 = new WorldObject();
        o2.setPhysicsState(new PhysicsStateNormal(o2, new SimplePhysicsProperties(1f), new Position(new Vector4f(0.0f, 1f, 0, 1)), new Vector4f(0, 0, 0, 0), new Vector4f(0, 0, 0, 0)));
        o2.setCollisionModel(new CollisionSphere(o2, 0.3f));
        PhysicsController physicsController = new PhysicsController();
        System.out.println(o1.getPosition().getLocation()+", "+o2.getPosition().getLocation());
        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            physicsController.applyPhysics(Arrays.asList(o1, o2), 0.01f);
            System.out.println(o2.getPosition().getLocation());
        }
        System.out.println();
        System.out.println(System.currentTimeMillis()-time);
    }

    public Position getPosition() {
        return getPhysicsState().getPosition();
    }
}
