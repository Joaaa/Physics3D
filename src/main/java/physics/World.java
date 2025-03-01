package physics;

import rendering.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class World {

    private List<WorldObject> worldObjects;

    public World() {
        worldObjects = new ArrayList<>();
    }

    public World(List<WorldObject> worldObjects) {
        this.worldObjects = worldObjects;
    }

    public void addWorldObject(WorldObject worldObject) {
        this.worldObjects.add(worldObject);
    }

    public void addWorldObjects(List<WorldObject> worldObjects) {
        this.worldObjects.addAll(worldObjects);
    }

    public List<WorldObject> getWorldObjects() {
        return worldObjects;
    }
}
