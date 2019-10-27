package physics;

import Utilities.Vector4f;
import collision.CollisionPoint;
import collision.CollisionResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GlobalPhysicsState {

    private final List<PhysicsState> physicsStates;

    public GlobalPhysicsState(List<PhysicsState> physicsStates) {
        this.physicsStates = physicsStates;
    }

    public List<PhysicsState> getPhysicsStates() {
        return physicsStates;
    }

    public GlobalPhysicsState applyDerivative(GlobalStateDerivative globalStateDerivative, float dt) {
        List<StateDerivative> derivatives = globalStateDerivative.getStateDerivatives();
        return new GlobalPhysicsState(
                IntStream.range(0, physicsStates.size())
                .mapToObj(i -> physicsStates.get(i).applyStateDerivative(derivatives.get(i), dt))
                .collect(Collectors.toList())
        );
    }

    public GlobalStateDerivative getDerivative() {
        for(PhysicsState physicsState: physicsStates) {
            physicsState.getWorldObject().setPhysicsState(physicsState);
        }

        List<StateDerivative> stateDerivatives = physicsStates.stream().map(s -> new StateDerivative(s.getLinSpeed(), s.getAngSpeed())).collect(Collectors.toList());

        for (int i = 0; i < physicsStates.size(); i++) {
            for(Force force: physicsStates.get(i).getInherentForces()) {
                stateDerivatives.get(i).addForce(force);
            }
        }
        
        for (int i = 0; i < physicsStates.size(); i++) {
            for (int j = i+1; j < physicsStates.size(); j++) {
                addCollisionForces(physicsStates.get(i), physicsStates.get(j), stateDerivatives.get(i), stateDerivatives.get(j));
            }
        }

        return new GlobalStateDerivative(stateDerivatives);
    }

    private void addCollisionForces(PhysicsState state1, PhysicsState state2, StateDerivative der1, StateDerivative der2) {
        if(state1.getWorldObject().getCollisionModel() == null || state2.getWorldObject().getCollisionModel() == null)
            return;
        CollisionResult result = state1.getWorldObject().getCollisionModel().checkCollision(state2.getWorldObject().getCollisionModel());
        for(CollisionPoint collisionPoint: result.getCollisionPoints()) {
            float collisionStrength = 1e3f*Math.min(state1.getMass(), state2.getMass());
            float collisionFriction = 50*Math.min(state1.getMass(), state2.getMass());

            Vector4f normal = collisionPoint.getNormal();
            if(normal.dotProduct(state1.getPosition().getLocation().add(collisionPoint.getPoint().getInverted())) < 0)
                normal = normal.getInverted();

            Vector4f relSpeed = state2.getSpeedAt(collisionPoint.getPoint()).add(state1.getSpeedAt(collisionPoint.getPoint()).getInverted());
            float speed = normal.dotProduct(relSpeed);

            Vector4f forceVector =
                    normal.multiply(collisionPoint.getCollisionDepth()*collisionStrength + speed*collisionFriction);

            Force collisionForce1 = new Force(forceVector);
            der1.addForce(collisionForce1);

            Force collisionForce2 = new Force(forceVector.getInverted());
            der2.addForce(collisionForce2);

            der1.addTorque(Torque.fromForce(collisionForce1, collisionPoint.getPoint().add(state1.getPosition().getLocation().getInverted())));
            der2.addTorque(Torque.fromForce(collisionForce2, collisionPoint.getPoint().add(state2.getPosition().getLocation().getInverted())));
        }
    }

}
