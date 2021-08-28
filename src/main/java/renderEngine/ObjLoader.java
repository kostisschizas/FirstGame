package renderEngine;

import models.RawModel;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {

    public static RawModel objToRawModel(File file, Loader loader){
        AIPropertyStore settings = Assimp.aiCreatePropertyStore();
        Assimp.aiSetImportPropertyInteger(settings, Assimp.AI_CONFIG_PP_SLM_VERTEX_LIMIT,65535);

        AIScene scene = Assimp.aiImportFile(file.toString(),Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_FlipUVs
                | Assimp.aiProcess_CalcTangentSpace | Assimp.aiProcess_LimitBoneWeights
                | Assimp.aiProcess_SplitLargeMeshes | Assimp.aiProcess_OptimizeMeshes
                | Assimp.aiProcess_JoinIdenticalVertices);

        Assimp.aiReleasePropertyStore(settings);
        if (scene==null){
            return null;
        }
        int numMeshes=scene.mNumMeshes();
        PointerBuffer aiMeshes = scene.mMeshes();

        if (aiMeshes==null){
            throw new RuntimeException("aiMeshes Pointbuffer is null");
        }

        AIMesh[] meshes = new AIMesh[numMeshes];

        for (int i=0; i<numMeshes; i++){
            AIMesh aiMesh =AIMesh.create(aiMeshes.get(i));
            meshes[i]=aiMesh;
        }


        //incomplete
        //System.out.println(meshes.length);
        return loader.loadToVAO(processVertices(meshes[0]),processTexCoords(meshes[0]),processIndices(meshes[0]),processNormals(meshes[0]));

    }

    private static float[] processVertices(AIMesh mesh){
        AIVector3D.Buffer aiVertices=mesh.mVertices();
        int remaining= aiVertices.remaining();
        float[] vertices = new float[remaining*3];
        for (int i=0;i<remaining;i++){
            AIVector3D vector = aiVertices.get();
            vertices[3*i]=vector.x();
            vertices[3*i+1]=vector.y();
            vertices[3*i+2]=vector.z();
        }
        return vertices;
    }

    private static int[] processIndices(AIMesh mesh){
        int numFaces=mesh.mNumFaces();
        List<Integer> indicesList = new ArrayList<>();
        AIFace.Buffer aiFaces = mesh.mFaces();
        for (int i=0;i<numFaces;i++){
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.hasRemaining()){
                indicesList.add(buffer.get());
            }
        }
        return indicesList.stream().mapToInt(i -> i).toArray();
    }

    private static float[] processTexCoords(AIMesh mesh){
        AIVector3D.Buffer texCoords=mesh.mTextureCoords(0);
        List<Float> texCoordsList= new ArrayList<>();
        int numTexCoords=texCoords!=null ? texCoords.remaining() : 0;
        for (int i=0;i<numTexCoords;i++){
            AIVector3D vector = texCoords.get();
            texCoordsList.add(vector.x());
            texCoordsList.add(vector.y());
        }
        float[] texCoordsArray= new float[texCoordsList.size()];
        for (int i =0; i<texCoordsList.size();i++){
            texCoordsArray[i]=texCoordsList.get(i);
        }
        return texCoordsArray;
    }

    private static float[] processNormals(AIMesh mesh){
        AIVector3D.Buffer aiNormals = mesh.mNormals();
        int remaining = aiNormals.remaining();
        float[] normalsArray=new float[remaining*3];
        for (int i=0;i<remaining;i++){
            AIVector3D vector = aiNormals.get();
            normalsArray[3*i]=vector.x();
            normalsArray[3*i+1]=vector.y();
            normalsArray[3*i+2]=vector.z();
        }
        return normalsArray;
    }


}
