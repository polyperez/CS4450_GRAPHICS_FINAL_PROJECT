import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

/**
 *
 * @author Victor
 */
public class Camera {
    //3d vector to store the camera's position in
    private Vector3f position = null;
    private Vector3f lPosition = null;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    //private Vector3Float me;
    
    public Camera(float x, float y, float z){
       position = new Vector3f(x,y,z);
    }
    

    public void FPCameraController(float x, float y, float z)
    {
    //instantiate position Vector3f to the x y z params.
    position = new Vector3f(x, y, z);
    lPosition = new Vector3f(x,y,z);
    lPosition.x = 0f;
    lPosition.y = 15f;
    lPosition.z = 0f;
    }
    
public void yaw(float amount)
{
    //increment the yaw by the amount
    yaw += amount;
}

public void pitch(float amount)
{
    //increment the pitch by the amount param
    pitch -= amount;
}

public void walkForward(float distance)
{
position.x += distance * (float)Math.sin(Math.toRadians(yaw));
position.z -= distance * (float)Math.cos(Math.toRadians(yaw));

}

//moves the camera backward relative to its current rotation (yaw)
public void walkBackwards(float distance)
{
position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
position.z += distance * (float)Math.cos(Math.toRadians(yaw));
}

//strafes the camera left relative to its current rotation (yaw)
public void strafeLeft(float distance)
{
position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));

}

//strafes the camera right relative to its current rotation (yaw)
public void strafeRight(float distance)
{
position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
}

//moves the camera up relative to its current rotation (yaw)
public void moveUp(float distance)
{
position.y -= distance;
}
//moves the camera down
public void moveDown(float distance)
{
position.y += distance;
}

//translates and rotate the matrix so that it looks through the camera
//this does basically what gluLookAt() does
public void lookThrough()
{
//roatate the pitch around the X axis
glRotatef(pitch, 1.0f, 0.0f, 0.0f);
//roatate the yaw around the Y axis
glRotatef(yaw, 0.0f, 1.0f, 0.0f);
//translate to the position vector's location
glTranslatef(-position.x, -position.y, -position.z);
}
}