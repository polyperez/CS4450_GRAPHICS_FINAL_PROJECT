import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.FloatBuffer;

public class Main {

    private static FloatBuffer lightPosition;
    private static FloatBuffer whiteLight;
    private static FloatBuffer dimLight;
    
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

            if (Keyboard.isKeyDown(Keyboard.KEY_Q) || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                break;
            }

            camera.yaw(Mouse.getDX() * 0.1f);
            camera.pitch(Mouse.getDY() * 0.1f);

            if (Keyboard.isKeyDown(Keyboard.KEY_W)) camera.walkForward(0.3f);
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) camera.walkBackwards(0.3f);
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) camera.strafeLeft(0.3f);
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) camera.strafeRight(0.3f);
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) camera.moveUp(0.3f);
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) camera.moveDown(0.3f);

            camera.lookThrough();

            // Re-apply directional light after camera transform
            glLight(GL_LIGHT0, GL_POSITION, lightPosition);

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

        initLightArrays();

        // Very low global ambient so the "dark half" is still dimly visible
        glLightModel(GL_LIGHT_MODEL_AMBIENT, dimLight);

        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);

        // Keep light ambient at 0 so only lit faces get strong light
        glLight(GL_LIGHT0, GL_AMBIENT, BufferUtils.createFloatBuffer(4).put(new float[] {0f, 0f, 0f, 1f}));
        glLight(GL_LIGHT0, GL_AMBIENT, (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[] {0f, 0f, 0f, 1f}).flip());

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
    }

    private static void initLightArrays() {
        // Directional light coming from the +X side toward the world
        // w = 0.0f means directional light
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(1.0f).put(0.0f).put(0.0f).put(0.0f).flip();

        //MAIN LIGHT COLOR
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        //ambient light
        dimLight = BufferUtils.createFloatBuffer(4);
        dimLight.put(0.15f).put(0.15f).put(0.15f).put(1.0f).flip();
    }
}