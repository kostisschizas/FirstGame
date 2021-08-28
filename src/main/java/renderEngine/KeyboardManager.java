package renderEngine;

import entities.Camera;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardManager extends GLFWKeyCallback {

    private long window;
    private List<Integer> keysPressed;
    private Camera camera;

    public KeyboardManager(long window) {
        this.window = window;
        keysPressed=new ArrayList<>();
    }

    public KeyboardManager(long window, Camera camera) {
        this.window = window;
        this.camera = camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public String getSignature() {
        return super.getSignature();
    }

    @Override
    public void callback(long args) {
        super.callback(args);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action==GLFW_PRESS&&!keysPressed.contains(key)){
            keysPressed.add(key);
        } else if (action==GLFW_RELEASE&&keysPressed.contains(key)){
            keysPressed.remove(((Integer) key));
        }
        pressMethod();
    }

    @Override
    public void close() {
        super.close();
    }

    public void pressMethod(){
        if (keysPressed.contains(GLFW_KEY_ESCAPE)){
            glfwSetWindowShouldClose(window,true);
        }
        //camera
        if (camera!=null){
            if (keysPressed.contains(GLFW_KEY_W)){
                camera.move(new Vector3f(0,0,-0.2f));
            }
            if (keysPressed.contains(GLFW_KEY_S)){
                camera.move(new Vector3f(0,0,0.2f));
            }
            if (keysPressed.contains(GLFW_KEY_A)){
                camera.move(new Vector3f(-0.2f,0,0));
            }
            if (keysPressed.contains(GLFW_KEY_D)){
                camera.move(new Vector3f(0.2f,0,0));
            }
            if (keysPressed.contains(GLFW_KEY_SPACE)){
                camera.move(new Vector3f(0,0.2f,0));
            }
            if (keysPressed.contains(GLFW_KEY_LEFT_SHIFT)){
                camera.move(new Vector3f(0,-0.2f,0));
            }
        }
    }
}
