package shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL33.*;

public abstract class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public static FloatBuffer matrixBuffer= BufferUtils.createFloatBuffer(16);

    public ShaderProgram(InputStream vertexFile, InputStream fragmentFile) {
        vertexShaderID=loadShader(vertexFile,GL_VERTEX_SHADER);
        fragmentShaderID=loadShader(fragmentFile,GL_FRAGMENT_SHADER);
        programID=glCreateProgram();
        glAttachShader(programID,vertexShaderID);
        glAttachShader(programID,fragmentShaderID);
        bindAttributes();
        glLinkProgram(programID);
        glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected void bindAttribute(int attribute, String variableName){
        glBindAttribLocation(programID,attribute,variableName);
    }
    protected int getUniformLocation(String uniformName){
        return glGetUniformLocation(programID,uniformName);
    }

    protected abstract void getAllUniformLocations();

    public void start(){
        glUseProgram(programID);
    }

    public void stop(){
        glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        glDetachShader(programID,vertexShaderID);
        glDetachShader(programID,fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void loadFloat(int location, float value){
        glUniform1f(location, value);
    }
    protected void loadVector(int location, Vector3f value){
        glUniform3f(location,value.x,value.y, value.z);
    }
    protected void loadBoolean(int location, boolean value){
        float toLoad=0;
        if (value){
            toLoad=1;
        }
        glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4f value){
        value.get(matrixBuffer);
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    private static int loadShader(InputStream inputStream, int type){
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line=reader.readLine())!=null){
                shaderSource.append(line).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Could not load shader file!");
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderId=glCreateShader(type);
        glShaderSource(shaderId,shaderSource);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId,GL_COMPILE_STATUS)==GL_FALSE){
            System.out.println(glGetShaderInfoLog(shaderId,500));
            System.err.println("Could not compile shader");
            System.exit(-1);
        }
        return shaderId;
    }

}
