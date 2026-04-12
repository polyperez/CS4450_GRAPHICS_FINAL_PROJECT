import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {

    public static void main(String[] args) throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("CS4450 Final Project");
        Display.create();

        initGL();

        Chunk chunk = new Chunk(0, -10, -20);
        Camera camera = new Camera(0, 0, 5);

        Mouse.setGrabbed(true);

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();

            if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                break;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                break;
            }

            camera.yaw(Mouse.getDX() * 0.1f);
            camera.pitch(Mouse.getDY() * 0.1f);

            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                camera.walkForward(0.3f);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                camera.walkBackwards(0.3f);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                camera.strafeLeft(0.3f);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                camera.strafeRight(0.3f);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.moveUp(0.3f);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(0.3f);
            }

            camera.lookThrough();
            chunk.render();

            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    private static void initGL() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        float aspect = 640f / 480f;
        glFrustum(-aspect, aspect, -1, 1, 1, 1000);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
    }
}