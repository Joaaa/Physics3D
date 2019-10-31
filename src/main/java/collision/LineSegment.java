package collision;

import Utilities.Vector4f;

public class LineSegment {

    private final Vector4f start;
    private final Vector4f end;

    public LineSegment(Vector4f start, Vector4f end) {
        this.start = start;
        this.end = end;
    }

    public Vector4f getStart() {
        return start;
    }

    public Vector4f getEnd() {
        return end;
    }

}
