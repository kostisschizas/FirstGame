package shaders;

import entities.Camera;
import entities.Light;
import org.joml.Matrix4f;
import toolbox.Maths;

import java.io.File;
import java.io.InputStream;

public class StaticShader extends ShaderProgram{
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;

    public StaticShader(InputStream vertexFile, InputStream fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix=super.getUniformLocation("transformationMatrix");
        location_projectionMatrix=super.getUniformLocation("projectionMatrix");
        location_viewMatrix=super.getUniformLocation("viewMatrix");
        location_lightPosition=super.getUniformLocation("lightPosition");
        location_lightColor=super.getUniformLocation("lightColor");
        location_shineDamper=super.getUniformLocation("shineDamper");
        location_reflectivity=super.getUniformLocation("reflectivity");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2, "normals");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    public void loadCamera(Camera camera){
        Matrix4f matrix= Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,matrix);
    }
    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper,damper);
        super.loadFloat(location_reflectivity,reflectivity);
    }
}
