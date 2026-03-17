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
    
    initGL();
    
    while(!Display.isCloseRequested()){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        render();
        
        Display.update();
        Display.sync(60);
}
    
Display.destroy();
}

private static void initGL(){
    int GL_Projection = 0;
    
    glMatrixMode(GL_Projection);
    glLoadIdentity();
    glOrtho(-10,10,-10,10,-10,10);
    
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


