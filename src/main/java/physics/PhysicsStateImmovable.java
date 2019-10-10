package physics;

import Utilities.Vector4f;
import rendering.WorldObject;

public class PhysicsStateImmovable extends PhysicsState {

    private final Position position;

    public PhysicsStateImmovable(WorldObject worldObject, Position position) {
        super(worldObject);
        this.position = position;
    }

    @Override
    public PhysicsState applyStateDerivative(StateDerivative stateDerivative, float dt) {
        return this;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Vector4f getLinSpeed() {
        return new Vector4f(0, 0, 0, 0);
    }

    @Override
    public Vector4f getAngSpeed() {
        return new Vector4f(0, 0, 0, 0);
    }

}
