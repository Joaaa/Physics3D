package physics;

import rendering.FPSCounter;

public class PhysicsRunnerThread extends Thread {

    private final PhysicsController controller;
    private float simulationSpeed;
    private FPSCounter fpsCounter;
    private boolean running;

    private final Object updateLock = new Object();

    public PhysicsRunnerThread(PhysicsController controller, float simulationSpeed) {
        this.controller = controller;
        this.simulationSpeed = simulationSpeed;
        this.fpsCounter = new FPSCounter();
    }

    @Override
    public void run() {
        long lastUpdate = System.nanoTime();
        running = true;
        while(running) {
            long time = System.nanoTime();
            float dt = (time - lastUpdate) / (1e9f / simulationSpeed);
            dt = Math.min(dt, 0.001f);

            controller.update(dt);

            fpsCounter.update();
            lastUpdate = time;
        }
    }

    public void end() {
        this.running = false;
        yield();
    }

    public float getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(float simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    public int getFps() {
        return fpsCounter.getFps();
    }

    public World getWorld() {
        return controller.getWorld();
    }

}
