package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private StaticShader shader = new StaticShader(ClassLoader.getSystemClassLoader().getResourceAsStream("vertexShader.glsl"),ClassLoader.getSystemClassLoader().getResourceAsStream("fragmentShader.glsl"));
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(Light light, Camera camera){
        renderer.prepare();
        shader.start();
        shader.loadLight(light);
        shader.loadCamera(camera);
        renderer.render(entities);
        shader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity){
        TexturedModel texturedModel = entity.getModel();
        List<Entity> batch = entities.get(texturedModel);
        if (batch!=null){
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(texturedModel,newBatch);
        }
    }

    public void cleanUp(){
        shader.cleanUp();
    }

}
