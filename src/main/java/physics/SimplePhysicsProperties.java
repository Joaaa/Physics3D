package physics;

import Utilities.Vector4f;

public class SimplePhysicsProperties implements IPhysicsProperties {

    private final float mass, inertia;

    public SimplePhysicsProperties(float mass) {
        this.mass = mass;
        // Mass/6 for cube of size 1
        this.inertia = mass/6;
    }

    public float getMass() {
        return mass;
    }

    public float getInertia(Vector4f axis) {
        return inertia;
    }

}
