package collision;

import java.util.Collections;
import java.util.List;

public class CollisionResult {

    public final static CollisionResult EMPTY = new CollisionResult(Collections.emptyList());
    private final List<CollisionPoint> collisionPoints;

    public CollisionResult(List<CollisionPoint> collisionPoints) {
        this.collisionPoints = collisionPoints;
    }

    public List<CollisionPoint> getCollisionPoints() {
        return collisionPoints;
    }

}
