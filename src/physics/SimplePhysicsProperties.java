package physics;

import Utilities.Vector4f;

public class SimplePhysicsProperties implements IPhysicsProperties {

    private final float mass, inertia;

    public SimplePhysicsProperties(float mass) {
        this.mass = mass;
        this.inertia = mass/10;
    }

    public float getMass() {
        return mass;
    }

    public float getInertia(Vector4f axis) {
        return inertia;
    }

}
