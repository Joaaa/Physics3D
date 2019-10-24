package physics;

import Utilities.Vector4f;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PhysicsStateNormalTest {

    @Mock
    StateDerivative stateDerivative;

    @Test
    void test() {
        PhysicsState state = new PhysicsStateNormal(null, new SimplePhysicsProperties(10), new Position());
        Mockito.when(stateDerivative.getTorques()).thenReturn(Arrays.asList(new Torque(new Vector4f(1, 0, 0, 0))));
        Mockito.when(stateDerivative.getForces()).thenReturn(Collections.emptyList());
        Mockito.when(stateDerivative.getLinSpeed()).thenReturn(new Vector4f(0, 0, 0, 0));
        Mockito.when(stateDerivative.getAngSpeed()).thenReturn(new Vector4f(0, 0, 0, 0));
        PhysicsState result = state.applyStateDerivative(stateDerivative, 1);
        assertEquals(new Vector4f(0, 0, 0, 1), result.getPosition().getLocation());
        assertTrue(result.getSpeedAt(new Vector4f(0, 0, 1, 1)).y < 0);
        Vector4f pos = new Vector4f(0.3f, 0.4f, 0.5f, 1);
        assertTrue(result.getSpeedAt(pos).add(result.getSpeedAt(pos.getInverted())).getLength() < 0.00001f);
    }

}