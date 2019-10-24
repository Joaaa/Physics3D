package physics;

import java.util.List;

public class GlobalStateDerivative implements IStateDerivative<GlobalPhysicsState> {

    private final List<StateDerivative> stateDerivatives;

    public GlobalStateDerivative(List<StateDerivative> stateDerivatives) {
        this.stateDerivatives = stateDerivatives;
    }

    public List<StateDerivative> getStateDerivatives() {
        return stateDerivatives;
    }

    @Override
    public GlobalPhysicsState applyTo(GlobalPhysicsState globalPhysicsState, float dt) {
        return null;
    }
}
