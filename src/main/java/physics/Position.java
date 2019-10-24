package physics;

import Utilities.Matrix4f;
import Utilities.Vector4f;

public class Position {

    private final Vector4f location;
    private final Matrix4f rotationMatrix;

    public Position() {
        this(new Vector4f(0, 0, 0, 1));
    }

    public Position(Vector4f location) {
        this(location, Matrix4f.getIdentityMatrix());
    }

    public Position(Vector4f location, Matrix4f rotationMatrix) {
        this.location = location;
        this.rotationMatrix = rotationMatrix;
    }

    public Vector4f getLocation() {
        return location;
    }

    public Matrix4f getRotationMatrix() {
        return rotationMatrix;
    }

    public Position applyTranslation(Vector4f translation) {
        return new Position(this.location.add(translation), this.rotationMatrix);
    }

    public Position applyRotation(Vector4f rotation) {
        if(new Vector4f(0, 0, 0, 1).leftMult(this.rotationMatrix).getLength() > 0.0001)
            throw new IllegalStateException("Invalid rotation matrix");
        Matrix4f newRotation = this.rotationMatrix;
        float angle = rotation.getLength();
        if(angle > 0)
            newRotation = newRotation.leftMult(Matrix4f.getRotationMatrix(rotation, angle));

        return new Position(this.location, newRotation);
    }

    public Position applyChange(Vector4f translation, Vector4f rotation) {
        return applyRotation(rotation).applyTranslation(translation);
    }

    public Matrix4f getTransformation() {
        return getRotationMatrix().leftMult(Matrix4f.getTranslationMatrix(getLocation()));
    }
}
