package rendering;

public class FPSCounter {

    private final static float FPS_UPDATE_INTERVAl = 0.1f;

    private int fps;
    private long lastUpdate;
    private int frames;

    public FPSCounter() {
        lastUpdate = -1;
        frames = 0;
        fps = 0;
    }

    public void update() {
        long time = System.nanoTime();
        if(lastUpdate == -1) {
            lastUpdate = time;
        }

        frames++;

        if((time-lastUpdate)/1e9f > FPS_UPDATE_INTERVAl) {
            fps = Math.round(frames/FPS_UPDATE_INTERVAl);
            frames = 0;
            lastUpdate = time;
        }
    }

    public int getFps() {
        return fps;
    }

}
