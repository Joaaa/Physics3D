package physics;

import Utilities.Matrix4f;
import Utilities.Vector4f;

public class Position {

    private final Vector4f location;
    private final Matrix4f rotation;

    public Position() {
        this(new Vector4f(0, 0, 0, 1));
    }

    public Position(Vector4f location) {
        this(location, Matrix4f.getIdentityMatrix());
    }

    public Position(Vector4f location, Matrix4f rotation) {
        this.location = location;
        this.rotation = rotation;
    }

    public Vector4f getLocation() {
        return location;
    }

    public Matrix4f getRotation() {
        return rotation;
    }

    public Position applyTranslation(Vector4f translation) {
        return new Position(this.location.add(translation), this.rotation);
    }

    public Position applyRotation(Vector4f rotation) {
        Matrix4f newRotation = this.rotation;
        float angle = rotation.getLength();
        if(angle > 0)
            newRotation = this.rotation.leftMult(Matrix4f.getRotationMatrix(rotation, angle));

        return new Position(this.location, newRotation);
    }

    public Position applyChange(Vector4f translation, Vector4f rotation) {
        Matrix4f newRotation = this.rotation;
        float angle = rotation.getLength();
        if(angle > 0)
            newRotation = this.rotation.leftMult(Matrix4f.getRotationMatrix(rotation, angle));

        return new Position(this.location.add(translation), newRotation);
    }

    public Matrix4f getTransformation() {
        return getRotation().leftMult(Matrix4f.getTranslationMatrix(getLocation()));
    }
}
