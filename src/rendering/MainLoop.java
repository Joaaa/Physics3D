package rendering;

import Utilities.*;
import collision.CollisionMesh;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import physics.*;

import java.util.Arrays;
import java.util.List;

public class MainLoop {

    private final List<WorldObject> worldObjects;
    private final ShaderProgram shaderProgram;
    private final Camera camera;
    private final Vector4f lightDirection;
    private final ShadowMap shadowMap;

    public MainLoop() {
        Display.instance.init(800, 600);
        worldObjects = Arrays.asList(
                createCube(10, false, new Vector4f(0, 0, 0, 1), 0),
                createCube(1, true, new Vector4f(0, 10, 5, 1), 1)
        );
        this.shaderProgram = new ShaderProgram("/shaders/simple.vert", "/shaders/simple.frag");
        this.camera = new Camera();
        camera.setPosition(new Vector4f(-5, 10, 10, 1));
        camera.setRotation(new Vector4f(-0.6f, -0.5f, 0, 0));
        lightDirection = new Vector4f(2, -3, -1, 0).normalize();
        shadowMap = new ShadowMap(1024);

        draw();
        Display.instance.update();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long lastUpdate = System.currentTimeMillis();
        while(!Display.instance.isClosed()) {
            long time = System.currentTimeMillis();
            float dt = Math.min((time-lastUpdate)/10000f, 0.01f);
            new PhysicsController().applyPhysics(worldObjects, dt);
            draw();
            Display.instance.update();
            ErrorChecker.check();
            lastUpdate = time;
        }
    }

    private void draw() {
        shadowMap.start();
        Matrix4f PShadow = Matrix4f.getOrthoProjectionMatrix(20, 20, -20, 20);
        Matrix4f VShadow = Matrix4f.getRotationMatrix(new Vector4f(0, 1, 0, 1),
                (float) -Math.atan2(-lightDirection.x, -lightDirection.z))
                .leftMult(
                        Matrix4f.getRotationMatrix(new Vector4f(1, 0, 0, 1),
                                (float) -Math.atan2(lightDirection.y, Math.sqrt(lightDirection.x*lightDirection.x+lightDirection.z*lightDirection.z)))
                );
        Matrix4f VPShadow = VShadow.leftMult(PShadow);
        for(WorldObject object: worldObjects) {
            drawObjectShadow(object, VPShadow);
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.instance.getWidth(), Display.instance.getHeight());
        GL11.glClearColor(0.4f, 0.6f,0.8f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        shaderProgram.use();
        for(WorldObject object: worldObjects) {
            drawObject(object, camera.getViewMatrix(), camera.getProjectionMatrix(), camera.getInverseMatrix(), shadowMap.getTexture(), VPShadow);
        }
//        TextureDrawer.drawTexture(shadowMap.getTexture(), Display.instance.getWidth(), Display.instance.getHeight());
    }

    private void drawObjectShadow(WorldObject object, Matrix4f VP) {
        IMesh mesh = object.getMesh();
        Matrix4f M = mesh.getModelMatrix().leftMult(object.getPosition().getTransformation());
        GL20.glUniformMatrix4fv(ShadowMap.shadowShader.getUniformLocation("MVP"), false, M.leftMult(VP).toFloatBuffer());
        VertexBuffer vertexBuffer = mesh.getVertexBuffer();
        vertexBuffer.vertexAttribPointer(ShadowMap.shadowShader.getLocation("pos"), "Position");
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexBuffer.getVertexAmount());
    }

    private void drawObject(WorldObject object, Matrix4f V, Matrix4f P, Matrix4f VP_inv, Texture shadowTexture, Matrix4f VPShadow) {
        IMesh mesh = object.getMesh();
        Matrix4f M = mesh.getModelMatrix().leftMult(object.getPosition().getTransformation());
        GL20.glUniformMatrix4fv(shaderProgram.getUniformLocation("M"), false, M.toFloatBuffer());
        GL20.glUniformMatrix4fv(shaderProgram.getUniformLocation("V"), false, V.toFloatBuffer());
        GL20.glUniformMatrix4fv(shaderProgram.getUniformLocation("VP_inv"), false, VP_inv.toFloatBuffer());
        GL20.glUniformMatrix4fv(shaderProgram.getUniformLocation("P"), false, P.toFloatBuffer());
        shadowTexture.bindToLocation(shaderProgram.getUniformLocation("shadowTexture"), 0);
        GL20.glUniformMatrix4fv(shaderProgram.getUniformLocation("shadowMatrix"), false, VPShadow.toFloatBuffer());
        GL20.glUniform3f(shaderProgram.getUniformLocation("lightDir"), lightDirection.x, lightDirection.y, lightDirection.z);
        VertexBuffer vertexBuffer = mesh.getVertexBuffer();
        vertexBuffer.vertexAttribPointer(shaderProgram.getLocation("pos"), "Position");
        vertexBuffer.vertexAttribPointer(shaderProgram.getLocation("normal"), "Normal");
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexBuffer.getVertexAmount());
    }

    private static WorldObject createCube(float size, boolean moveable, Vector4f location, float mass) {
        WorldObject object = new WorldObject();
        if(moveable)
            object.setPhysicsState(new PhysicsStateNormal(object, new SimplePhysicsProperties(mass), new Position(location)));
        else
            object.setPhysicsState(new PhysicsStateImmovable(object, new Position(location)));
        object.setCollisionModel(CollisionMesh.createCube(object, size));
        object.setMesh(new CubeMesh(size, size, size));

        return object;
    }

    public static void main(String[] args) {
        new MainLoop();
    }

}
