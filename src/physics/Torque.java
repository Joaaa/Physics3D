package physics;

import Utilities.Vector4f;

public class Torque {

    private final Vector4f torque;

    public Torque(Vector4f torque) {
        this.torque = torque;
    }

    public Vector4f getTorque() {
        return torque;
    }

    /**
     * Get the resulting torque from a force.
     *
     * @param force The force
     * @param r The vector from center of mass to the force
     * @return
     */
    public static Torque fromForce(Force force, Vector4f r) {
        return new Torque(Vector4f.crossProduct(r, force.getForce()));
    }

}
