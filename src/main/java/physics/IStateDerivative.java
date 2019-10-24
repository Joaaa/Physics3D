package physics;

public interface IStateDerivative<T extends IState> {

    T applyTo(T globalPhysicsState, float dt);

}
