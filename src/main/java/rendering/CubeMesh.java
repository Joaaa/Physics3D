package rendering;

import Utilities.Utilities;
import Utilities.VertexBuffer;
import Utilities.Matrix4f;
import Utilities.Vector4f;

public class CubeMesh implements IMesh {

    private static VertexBuffer buffer;

    private final Matrix4f modelMatrix;

    public CubeMesh(float xSize, float ySize, float zSize) {
        this.modelMatrix = Matrix4f.getScaleMatrix(new Vector4f(xSize, ySize, zSize, 1));

        if(buffer == null) {
            buffer = new VertexBuffer();
            buffer.addAttribute("Position", 3);
            buffer.addAttribute("Normal", 3);
            buffer.addAttribute("Uv", 2);
            Utilities.loadObjectToBuffer(getClass().getClassLoader().getResource("cube.obj").getFile(), buffer);
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
