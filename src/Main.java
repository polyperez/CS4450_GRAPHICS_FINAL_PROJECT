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
    
    Cube cube = new Cube();
    float angle = 0;
    
    while(!Display.isCloseRequested()){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //render();
        
        glLoadIdentity();
        glTranslatef(0,0,-5);
        glRotatef(angle,1,1,0);
        
        System.out.println("drawing cube");
        cube.draw();// IMPORTANT
        
        angle += 0.5f;
        
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


