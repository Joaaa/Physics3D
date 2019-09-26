package rendering;

import Utilities.VertexBuffer;
import Utilities.Matrix4f;

public interface IMesh {

    public Matrix4f getModelMatrix();

    public VertexBuffer getVertexBuffer();

}
