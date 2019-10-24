package physics;

public interface IState<OBJECT, DERIVATIVE extends IStateDerivative> {

    public void setState(OBJECT object);

    public DERIVATIVE getDerivative();

    public IState<OBJECT, DERIVATIVE> applyDerivative(DERIVATIVE stateDerivative, float dt);

}
