package entities;

import org.joml.Vector3f;
import renderEngine.KeyboardManager;

public class Camera {

    private Vector3f position;
    private float pitch=0;
    private float yaw=0;
    private float roll=0;

    public Camera(){
        position=new Vector3f(0,0,0);
    }

    public void move(Vector3f dx){
        position.add(dx);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
