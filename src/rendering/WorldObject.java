package rendering;

import Utilities.Vector4f;
import collision.CollisionMesh;
import collision.CollisionModel;
import physics.*;

import java.awt.*;
import java.util.Arrays;

public class WorldObject {

    private CollisionModel collisionModel;
    private PhysicsState physicsState;
    private IMesh mesh;

    public WorldObject() {
        this.physicsState = new PhysicsStateImmovable(this, new Position());
    }

    public void setCollisionModel(CollisionModel collisionModel) {
        this.collisionModel = collisionModel;
    }

    public void setPhysicsState(PhysicsState physicsState) {
        this.physicsState = physicsState;
    }

    public void setMesh(IMesh mesh) {
        this.mesh = mesh;
    }

    /**
     * Return the collision model, or null if none is present.
     * @return
     */
    public CollisionModel getCollisionModel() {
        return collisionModel;
    }

    public PhysicsState getPhysicsState() {
        return physicsState;
    }

    public IMesh getMesh() {
        return mesh;
    }

    public static void main(String[] args) {
//        WorldObject o1 = new WorldObject();
////        o1.setPhysicsState(new PhysicsStateImmovable(o1, new Position()));
//        o1.setPhysicsState(new PhysicsStateNormal(o1, new SimplePhysicsProperties(1f), new Position(new Vector4f(2, 0.5f, 0, 1)), new Vector4f(2, 1, 0, 0), new Vector4f(0, 0, 0, 0)));
//        o1.setCollisionModel(new CollisionSphere(o1, 0.3f));
//        WorldObject o2 = new WorldObject();
//        o2.setPhysicsState(new PhysicsStateNormal(o2, new SimplePhysicsProperties(1f), new Position(new Vector4f(3.1f, 0.3f, 0, 1)), new Vector4f(0, 0, 0, 0), new Vector4f(0, 0, 0, 0)));
//        o2.setCollisionModel(new CollisionSphere(o2, 0.3f));
//        WorldObject o3 = new WorldObject();
//        o3.setPhysicsState(new PhysicsStateImmovable(o3, new Position(new Vector4f(3.1f, -3f, 0f, 1f))));
//        o3.setCollisionModel(new CollisionSphere(o3, 3f));
//
//        PhysicsController physicsController = new PhysicsController();
//        System.out.println(o1.getPosition().getLocation()+", "+o2.getPosition().getLocation());
//        long time = System.currentTimeMillis();
//        TestGui testGui = new TestGui(g -> {
//            g.fillOval((int) ((o1.getPosition().getLocation().x-0.3)*100)+100, (int) ((-o1.getPosition().getLocation().y-0.3)*100)+100, 60, 60);
//            g.fillOval((int) ((o2.getPosition().getLocation().x-0.3)*100)+100, (int) ((-o2.getPosition().getLocation().y-0.3)*100)+100, 60, 60);
//            g.fillOval((int) ((o3.getPosition().getLocation().x-3)*100)+100, (int) ((-o3.getPosition().getLocation().y-3)*100)+100, 600, 600);
//        });
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < 1000; i++) {
//            physicsController.applyPhysics(Arrays.asList(o1, o2, o3), 0.01f);
//            System.out.println(o2.getPosition().getLocation());
//            testGui.repaint();
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println();
//        System.out.println(System.currentTimeMillis()-time);

        WorldObject o1 = new WorldObject();
        o1.setPhysicsState(new PhysicsStateImmovable(o1, new Position(new Vector4f(3f, -3f, 0, 1))));
//        o1.setPhysicsState(new PhysicsStateNormal(o1, new SimplePhysicsProperties(1f), new Position(new Vector4f(2, 0.5f, 0, 1)), new Vector4f(2, 1, 0, 0), new Vector4f(0, 0, 0, 0)));
        o1.setCollisionModel(CollisionMesh.createCube(o1, 2));
        WorldObject o2 = new WorldObject();
        o2.setPhysicsState(new PhysicsStateNormal(o2, new SimplePhysicsProperties(1f), new Position(new Vector4f(3f, -1f, 0, 1)), new Vector4f(0, 0, 0, 0), new Vector4f(0, 0, 0, 0)));
        o2.setCollisionModel(CollisionMesh.createCube(o2, 1));

        System.out.println(o1.getPosition().getLocation()+", "+o2.getPosition().getLocation());
        TestGui testGui = new TestGui(g -> {
            g.setColor(Color.BLACK);
            g.fillRect((int) ((o1.getPosition().getLocation().x-1)*100)+100, (int) ((-o1.getPosition().getLocation().y-1)*100)+100, 200, 200);
            g.fillRect((int) ((o2.getPosition().getLocation().x-0.5)*100)+100, (int) ((-o2.getPosition().getLocation().y-0.5)*100)+100, 100, 100);
            System.out.println("paint");
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis();
        PhysicsController physicsController = new PhysicsController();
        for (int i = 0; i < 10000; i++) {
            physicsController.applyPhysics(Arrays.asList(o1, o2), 0.001f);
            System.out.println(o2.getPosition().getLocation());
            testGui.repaint();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis()-time);
    }

    public Position getPosition() {
        return getPhysicsState().getPosition();
    }
}
