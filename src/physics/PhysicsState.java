package physics;

import Utilities.Vector4f;
import rendering.WorldObject;

import java.util.Collections;
import java.util.List;

public abstract class PhysicsState {

    private final WorldObject worldObject;

    protected PhysicsState(WorldObject worldObject) {
        this.worldObject = worldObject;
    }

    public abstract PhysicsState applyStateDerivative(StateDerivative stateDerivative, float dt);

    public abstract Position getPosition();

    public abstract Vector4f getLinSpeed();

    public abstract Vector4f getAngSpeed();

    public WorldObject getWorldObject() {
        return worldObject;
    }

    /**
     * Mainly gravity.
     *
     * @return
     */
    public List<Force> getInherentForces() {
        return Collections.emptyList();
    }

}
