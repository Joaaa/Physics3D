package physics;

import rendering.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class World {

    private ArrayList<WorldObject> worldObjects;

    public World() {
        worldObjects = new ArrayList<>();
    }

    public void addWorldObject(WorldObject worldObject) {
        this.worldObjects.add(worldObject);
    }

    public void addWorldObjects(List<WorldObject> worldObjects) {
        this.worldObjects.addAll(worldObjects);
    }

    public ArrayList<WorldObject> getWorldObjects() {
        return worldObjects;
    }
}
