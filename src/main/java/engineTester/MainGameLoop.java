package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.joml.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import textures.ModelTexture;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class MainGameLoop {

    private static long window;
    public static InputStream vertexFile = ClassLoader.getSystemClassLoader().getResourceAsStream("vertexShader.glsl");
    public static InputStream fragmentFile = ClassLoader.getSystemClassLoader().getResourceAsStream("fragmentShader.glsl");

    public static void main(String[] args){
        Random random =new Random();

        DisplayManager.createDisplay();
        window=DisplayManager.getWindow();

        KeyboardManager keyboardManager=new KeyboardManager(window);
        glfwSetKeyCallback(window,keyboardManager);

        Loader loader = new Loader();

        Camera camera=new Camera();
        keyboardManager.setCamera(camera);
        RawModel model = null;
        try {
            model = ObjLoader.objToRawModel(new File(ClassLoader.getSystemClassLoader().getResource("stall.obj").toURI()),loader);
        } catch (URISyntaxException e) {
            System.err.println("Could not find file!");
            e.printStackTrace();
            System.exit(-1);
        }
        ModelTexture texture=loader.loadTexture("stallTexture.png");
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity[] entities = new Entity[100];
        for (int i=0;i<100;i++){
            entities[i]=new Entity(texturedModel,new Vector3f(random.nextFloat()*100,random.nextFloat(),random.nextFloat()*100),random.nextFloat(),random.nextFloat(),random.nextFloat(),1);
        }

        Light light = new Light(new Vector3f(0,0,0),new Vector3f(1,1,1));
        MasterRenderer renderer = new MasterRenderer();
        while (!glfwWindowShouldClose(window)){
            for (Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.render(light,camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
