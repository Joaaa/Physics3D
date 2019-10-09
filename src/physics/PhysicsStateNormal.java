package physics;

import Utilities.Vector4f;
import rendering.WorldObject;

import java.util.Collections;
import java.util.List;

public class PhysicsStateNormal extends PhysicsState {

    private final IPhysicsProperties properties;
    private final Position position;
    private final Vector4f linMomentum, angMomentum;

    public PhysicsStateNormal(WorldObject worldObject, IPhysicsProperties properties, Position position) {
        this(worldObject, properties, position, new Vector4f(0, 0, 0, 0), new Vector4f(0, 0, 0, 0));
    }

    public PhysicsStateNormal(WorldObject worldObject, IPhysicsProperties properties, Position position, Vector4f linMomentum, Vector4f angMomentum) {
        super(worldObject);
        this.properties = properties;
        this.position = position;
        this.linMomentum = linMomentum;
        this.angMomentum = angMomentum;
    }

    /**
     * Return a new PhysicsState obtained by applying the provided derivative for the specified duration.
     * @param stateDerivative
     * @param dt
     * @return
     */
    @Override
    public PhysicsStateNormal applyStateDerivative(StateDerivative stateDerivative, float dt) {
        Position newPosition = position.applyChange(stateDerivative.getLinSpeed().multiply(dt), stateDerivative.getAngSpeed().multiply(dt));

        Vector4f newLinMomentum = linMomentum;
        for(Force force: stateDerivative.getForces()) {
            newLinMomentum = newLinMomentum.add(force.getForce().multiply(dt));
        }

        Vector4f newAngMomentum = angMomentum;
        for(Torque torque: stateDerivative.getTorques()) {
            newAngMomentum = newAngMomentum.add(torque.getTorque().multiply(dt));
        }

        return new PhysicsStateNormal(getWorldObject(), properties, newPosition, newLinMomentum, newAngMomentum);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Vector4f getLinSpeed() {
        return linMomentum.multiply(1/properties.getMass());
    }

    @Override
    public Vector4f getAngSpeed() {
        angMomentum.w = 0;
        return angMomentum.multiply(1/properties.getInertia(angMomentum));
    }

    @Override
    public List<Force> getInherentForces() {
        return Collections.singletonList(new Force(new Vector4f(0, (float) (-9.81*properties.getMass()), 0, 0)));
    }

    @Override
    public float getMass() {
        return properties.getMass();
    }
}