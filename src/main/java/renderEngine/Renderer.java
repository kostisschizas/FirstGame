package renderEngine;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import shaders.StaticShader;

import static org.lwjgl.opengl.GL33.*;

public class Renderer {
    private static final float FOV=70;
    private static final float NEAR_PLANE=0.1f;
    private static final float FAR_PLANE=1000;

    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader){
        projectionMatrix=new Matrix4f();
        projectionMatrix.perspective(FOV,(float) DisplayManager.WIDTH/(float) DisplayManager.HEIGHT,NEAR_PLANE,FAR_PLANE);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare(){
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(1,0, 0, 1);
    }

    public void render(Entity entity, StaticShader shader){
        TexturedModel texturedModel = entity.getModel();
        RawModel model=texturedModel.getRawModel();
        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        Matrix4f transformationMatrix= new Matrix4f()
                .identity()
                .translate(entity.getPosition())
                .rotateX((float) Math.toRadians(entity.getRotX()))
                .rotateY((float) Math.toRadians(entity.getRotY()))
                .rotateZ((float) Math.toRadians(entity.getRotZ()))
                .scale(entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
        glDrawElements(GL_TRIANGLES, model.getVertexCount(),GL_UNSIGNED_INT,0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
