package physics;

import Utilities.Vector4f;

public class Force {

    private final Vector4f force;

    public Force(Vector4f force) {
        this.force = force;
    }

    public Vector4f getForce() {
        return force;
    }

    @Override
    public String toString() {
        return "Force: "+force;
    }
}
