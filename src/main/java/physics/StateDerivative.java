package physics;

import Utilities.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateDerivative {

    private List<Force> forces;
    private List<Torque> torques;
    private final Vector4f linSpeed;
    private final Vector4f angSpeed;

    public StateDerivative(Vector4f linSpeed, Vector4f angSpeed) {
        this.forces = new ArrayList<>();
        this.torques = new ArrayList<>();
        this.linSpeed = linSpeed;
        this.angSpeed = angSpeed;
    }

    public void addForce(Force force) {
        this.forces.add(force);
    }

    public void addTorque(Torque torque) {
        this.torques.add(torque);
    }

    public List<Force> getForces() {
        return forces;
    }

    public List<Torque> getTorques() {
        return torques;
    }

    public Vector4f getLinSpeed() {
        return linSpeed;
    }

    public Vector4f getAngSpeed() {
        return angSpeed;
    }

    @Override
    public String toString() {
        return "Derivative: Speed="+angSpeed+", angSpeed="+angSpeed+", forces="+ Arrays.toString(forces.toArray());
    }

}
