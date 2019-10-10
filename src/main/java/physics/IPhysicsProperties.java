package physics;

import Utilities.Vector4f;

public interface IPhysicsProperties {

    float getMass();

    float getInertia(Vector4f axis);

}
