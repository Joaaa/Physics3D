package rendering;

import Utilities.Matrix4f;
import Utilities.Utilities;
import Utilities.Vector4f;
import Utilities.VertexBuffer;

public class SphereMesh implements IMesh {

    private static VertexBuffer buffer;

    private final Matrix4f modelMatrix;

    public SphereMesh(float radius) {
        this.modelMatrix = Matrix4f.getScaleMatrix(new Vector4f(radius, radius, radius, 1));

        if(buffer == null) {
            buffer = new VertexBuffer();
            buffer.addAttribute("Position", 3);
            buffer.addAttribute("Normal", 3);
            buffer.addAttribute("Uv", 2);
            Utilities.loadObjectToBuffer(getClass().getClassLoader().getResource("sphere.obj").getFile(), buffer);
            buffer.flush();
        }
    }

    @Override
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    @Override
    public VertexBuffer getVertexBuffer() {
        return buffer;
    }
}
