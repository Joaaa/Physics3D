package physics;

import Utilities.Matrix4f;
import Utilities.Vector4f;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    void positionTest() {
        Position position = new Position(new Vector4f(1, 0, 0, 1), Matrix4f.getIdentityMatrix());
        Position newPos = position.applyRotation(new Vector4f(0, 0.1f, 0, 1)).applyRotation(new Vector4f(0, 0, 0.1f, 1));
        System.out.println(newPos.getLocation());
        System.out.println(new Vector4f(0, 0, 0, 1).leftMult(newPos.getTransformation()));
        System.out.println(new Vector4f(1, 0, 0, 1).leftMult(newPos.getTransformation()));
    }

}
