package renderEngine;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class Renderer {
    private static final float FOV=70;
    private static final float NEAR_PLANE=0.1f;
    private static final float FAR_PLANE=1000;

    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public Renderer(StaticShader shader){
        this.shader=shader;
        projectionMatrix=new Matrix4f();
        projectionMatrix.perspective(FOV,(float) DisplayManager.WIDTH/(float) DisplayManager.HEIGHT,NEAR_PLANE,FAR_PLANE);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(1,0, 0, 1);
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for (TexturedModel model:entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity:batch){
                prepareInstance(entity);

            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel){
        RawModel model=texturedModel.getRawModel();
        glBindVertexArray(model.getVaoID());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        ModelTexture texture = texturedModel.getTexture();
        shader.loadShineVariables(texture.getShineDamper(),texture.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
    }

    private void unbindTexturedModel(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix= new Matrix4f()
                .identity()
                .translate(entity.getPosition())
                .rotateX((float) Math.toRadians(entity.getRotX()))
                .rotateY((float) Math.toRadians(entity.getRotY()))
                .rotateZ((float) Math.toRadians(entity.getRotZ()))
                .scale(entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        glDrawElements(GL_TRIANGLES, entity.getModel().getRawModel().getVertexCount(),GL_UNSIGNED_INT,0);
    }
}
