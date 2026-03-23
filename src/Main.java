import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Victor
 */
public class Main {
    
 public static void main(String[] args) throws LWJGLException {
    
    Display.setDisplayMode(new DisplayMode(640,480));
    Display.setTitle("CS4450 Final Project");
    Display.create();
    
    glEnable(GL_DEPTH_TEST);
    initGL();
    
    //Create cube
    Cube cube = new Cube();
    float angle = 0;
    
    //Create camera
    Camera camera = new Camera(0, 0, 0);
    Mouse.setGrabbed(true);
    
    while(!Display.isCloseRequested()){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //render();
        
        camera.yaw(Mouse.getDX() * 0.1f);
        camera.pitch(Mouse.getDY() * 0.1f);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) camera.walkForward(0.1f);
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) camera.walkBackwards(0.1f);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) camera.strafeLeft(0.1f);
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) camera.strafeRight(0.1f);
        
        glLoadIdentity();
        glTranslatef(0,0,-5);
        glRotatef(angle,1,1,0);
       
        glLoadIdentity();
        camera.lookThrough();
        
        //Cube stuff 
        System.out.println("drawing cube");
        cube.draw();// IMPORTANT
        
        angle += 0f; //Handle the rotation
        
        Display.update();
        //Display.sync(60);
}
    
Display.destroy();
}

private static void initGL(){  
    glMatrixMode(GL_PROJECTION); //Case sensitive
    glLoadIdentity();
    //glOrtho(-10,10,-10,10,-10,10);
    
    float aspect = 640f / 480f;
    glFrustum(-aspect, aspect, -1, 1, 1, 100);
    glMatrixMode(GL_MODELVIEW);
            }
private static void render(){
   glBegin(GL_QUADS);
   
   glColor3f(1,0,0);
   glVertex3f(-1,1,-1);
   glVertex3f(1,1,-1);
   glVertex3f(1,-1,-1);
   glVertex3f(-1,-1,-1);
   
   glEnd();
    }
  }


