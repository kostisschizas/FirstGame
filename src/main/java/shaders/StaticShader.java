package shaders;

import entities.Camera;
import org.joml.Matrix4f;
import toolbox.Maths;

import java.io.File;
import java.io.InputStream;

public class StaticShader extends ShaderProgram{
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader(InputStream vertexFile, InputStream fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix=super.getUniformLocation("transformationMatrix");
        location_projectionMatrix=super.getUniformLocation("projectionMatrix");
        location_viewMatrix=super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    public void loadCamera(Camera camera){
        Matrix4f matrix= Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,matrix);
    }
}
