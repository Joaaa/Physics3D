package physics;

import rendering.WorldObject;

import java.util.List;
import java.util.stream.Collectors;

public class PhysicsController {

    public void applyPhysics(List<WorldObject> worldObjects, float dt) {
        if(dt == 0)
            return;

        GlobalPhysicsState y = new GlobalPhysicsState(worldObjects.stream().map(WorldObject::getPhysicsState).collect(Collectors.toList()));
        GlobalStateDerivative k1 = y.getDerivative();
        GlobalStateDerivative k2 = y.applyDerivative(k1, dt/2).getDerivative();
        GlobalStateDerivative k3 = y.applyDerivative(k2, dt/2).getDerivative();
        GlobalStateDerivative k4 = y.applyDerivative(k3, dt).getDerivative();
        GlobalPhysicsState y1 = y.applyDerivative(k1, dt/6).applyDerivative(k2, dt/3).applyDerivative(k3, dt/3).applyDerivative(k4, dt/6);

        for (int i = 0; i < worldObjects.size(); i++) {
            worldObjects.get(i).setPhysicsState(y1.getPhysicsStates().get(i));
        }
    }

}
