package engineTester;

import entities.Camera;
import entities.Entity;
import models.TexturedModel;
import org.joml.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import textures.ModelTexture;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.lwjgl.glfw.GLFW.*;

public class MainGameLoop {

    private static long window;
    public static InputStream vertexFile = ClassLoader.getSystemClassLoader().getResourceAsStream("vertexShader.glsl");
    public static InputStream fragmentFile = ClassLoader.getSystemClassLoader().getResourceAsStream("fragmentShader.glsl");

    public static void main(String[] args){

        DisplayManager.createDisplay();
        window=DisplayManager.getWindow();

        KeyboardManager keyboardManager=new KeyboardManager(window);
        glfwSetKeyCallback(window,keyboardManager);

        Loader loader = new Loader();
        StaticShader shader=new StaticShader(vertexFile, fragmentFile);
        Renderer renderer = new Renderer(shader);

        Camera camera=new Camera();
        keyboardManager.setCamera(camera);
        RawModel model = null;
        RawModel model2= null;
        try {
            model = ObjLoader.objToRawModel(new File(ClassLoader.getSystemClassLoader().getResource("stall.obj").toURI()),loader);
            model2=ObjLoader.objToRawModel(new File(ClassLoader.getSystemClassLoader().getResource("untitled.obj").toURI()),loader);
        } catch (URISyntaxException e) {
            System.err.println("Could not find file!");
            e.printStackTrace();
            System.exit(-1);
        }
        ModelTexture texture=loader.loadTexture("stallTexture.png");
        ModelTexture texture2=loader.loadTexture("image.png");
        TexturedModel texturedModel = new TexturedModel(model, texture);
        TexturedModel texturedModel2 = new TexturedModel(model2,texture2);

        Entity entity = new Entity(texturedModel, new Vector3f(0,-1,-20),0,0,0,1);
        Entity cube = new Entity(texturedModel2,new Vector3f(-10,-1, -20),0,0,0,1);

        while (!glfwWindowShouldClose(window)){
            //entity.increasePosition(0,0,-0.04f);
            entity.setRotY(entity.getRotY()+0.2f);
            renderer.prepare();
            shader.start();
            shader.loadCamera(camera);
            renderer.render(entity, shader);
            renderer.render(cube,shader);
            shader.stop();
            DisplayManager.updateDisplay();
        }


        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
