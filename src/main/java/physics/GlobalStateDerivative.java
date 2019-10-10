package physics;

import java.util.List;

public class GlobalStateDerivative {

    private final List<StateDerivative> stateDerivatives;

    public GlobalStateDerivative(List<StateDerivative> stateDerivatives) {
        this.stateDerivatives = stateDerivatives;
    }

    public List<StateDerivative> getStateDerivatives() {
        return stateDerivatives;
    }

}
