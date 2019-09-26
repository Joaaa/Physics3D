package collision;

import Utilities.Vector4f;

import static org.junit.jupiter.api.Assertions.*;

class MeshTriangleTest {

    @org.junit.jupiter.api.Test
    void checkCollision() {
        MeshTriangle triangle = new MeshTriangle(new Vector4f(0, 0, 0, 1), new Vector4f(1, 0, 0, 1), new Vector4f(0, 1, 0, 1));
        assertNotNull(triangle.checkCollision(new Vector4f(0, 0, 1, 1), new Vector4f(0.5f, 0.5f, -0.5f, 1)));
        assertNull(triangle.checkCollision(new Vector4f(0, 0, 1, 1), new Vector4f(-0.1f, 0.5f, -0.5f, 1)));
        assertNull(triangle.checkCollision(new Vector4f(0, 0, 1, 1), new Vector4f(0.5f, -0.1f, -0.5f, 1)));

//        Triangle: [p1=(0.0, 0.0, -2.5, 1.0), p2=(0.0, 0.0, 2.5, 1.0), p3=(5.0, 0.0, -2.5, 1.0)], (1.0, 0.05, 0.0, 1.0), (0.9, -0.05, -0.1, 1.0)
        triangle = new MeshTriangle(new Vector4f(0.0f, 0.0f, -2.5f, 1.0f), new Vector4f(0.0f, 0.0f, 2.5f, 1.0f), new Vector4f(5.0f, 0.0f, -2.5f, 1.0f));
        assertNotNull(triangle.checkCollision(new Vector4f(1, 0.05f, 0, 1), new Vector4f(0.9f, -0.05f, -0.1f, 1)));
    }
}