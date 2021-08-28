package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import textures.ModelTexture;
import textures.TextureLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<ModelTexture> textures= new ArrayList<>();

    public RawModel loadToVAO(float[] positions,float[] textureCoords, int[] indices, float[] normals) {
        int vaoID=createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);

        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public ModelTexture loadTexture(String fileName){
        ModelTexture texture=null;
        try {
            texture= TextureLoader.loadTexture(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textures.add(texture);
        return texture;
    }

    public void cleanUp(){
        for (int vao:vaos){
            glDeleteVertexArrays(vao);
        }
        for (int vbo:vbos){
            glDeleteBuffers(vbo);
        }
        for (ModelTexture texture:textures){
            glDeleteTextures(texture.getTextureID());
        }
    }

    public void bindIndicesBuffer(int[] indices){
        int vboId=glGenBuffers();
        vbos.add(vboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer=storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer=BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private int createVAO(){
        int vaoID=glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
        int vboID=glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer=storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO(){
        glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer= BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
