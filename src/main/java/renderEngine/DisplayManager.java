package renderEngine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

    public static final int WIDTH=1280;
    public static final int HEIGHT=720;
    private static long window;

    public static void createDisplay(){
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize glfw");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);


        window=glfwCreateWindow(WIDTH,HEIGHT,"My Window",NULL,NULL);
        if (window==NULL){
            throw new RuntimeException("Failed to create the window");
        }
        try (MemoryStack stack=stackPush()){
            IntBuffer pWidth=stack.mallocInt(1);
            IntBuffer pHeight=stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth,pHeight);
            GLFWVidMode vidMode=glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidMode.width()-pWidth.get(0))/2,(vidMode.height()-pHeight.get(0))/2);

            glfwMakeContextCurrent(window);
            glfwSwapInterval(1);

            glfwShowWindow(window);

            GL.createCapabilities();
        }
    }

    public static long getWindow() {
        return window;
    }


    public static void updateDisplay(){
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public static void closeDisplay(){
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

}
