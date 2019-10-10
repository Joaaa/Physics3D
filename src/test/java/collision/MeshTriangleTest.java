package collision;

import Utilities.Vector4f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeshTriangleTest {

    @Test
    void checkCollision() {
        MeshTriangle triangle1 = new MeshTriangle(new Vector4f(0, 0, 0, 1), new Vector4f(1, 0, 0, 1), new Vector4f(0, 1, 0, 1));
        MeshTriangle triangle2 = new MeshTriangle(new Vector4f(0, 0, -1, 1), new Vector4f(1, 0, 1, 1), new Vector4f(0, 1, 1, 1));
        CollisionPoint p = triangle1.checkCollision(triangle2);
        assertNotNull(p);
        System.out.println(p.getPoint());
        System.out.println(p.getNormal());
        System.out.println(p.getCollisionDepth());

        CollisionPoint p2 = triangle2.checkCollision(triangle1);
        assertNotNull(p2);
        System.out.println(p2.getPoint());
        System.out.println(p2.getNormal());
        System.out.println(p2.getCollisionDepth());
        assertEquals(p.getPoint(), p2.getPoint());
        assertEquals(p.getNormal(), p2.getNormal());
        assertEquals(p.getCollisionDepth(), p2.getCollisionDepth());
    }

    @Test
    void checkNoCollision() {
        MeshTriangle triangle1 = new MeshTriangle(new Vector4f(0.5f, 9.5f, 4.5f, 1.0f), new Vector4f(0.5f, 10.5f, 4.5f, 1.0f), new Vector4f(0.5f, 9.5f, 5.5f, 1.0f));
        MeshTriangle triangle2 = new MeshTriangle(new Vector4f(-5.0f, -5.0f, 5.0f, 1.0f), new Vector4f(5.0f, -5.0f, 5.0f, 1.0f), new Vector4f(-5.0f, 5.0f, 5.0f, 1.0f));
        assertNull(triangle1.checkCollision(triangle2));
        assertNull(triangle2.checkCollision(triangle1));
    }


}