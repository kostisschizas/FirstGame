package toolbox;

import entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

    public static Matrix4f createViewMatrix(Camera camera){
        Vector3f position=camera.getPosition();
        return new Matrix4f()
                .identity()
                .rotateX((float) Math.toRadians(camera.getPitch()))
                .rotateY((float) Math.toRadians(camera.getYaw()))
                .rotateZ((float) Math.toRadians(camera.getRoll()))
                .translate(-position.x,-position.y, -position.z);
    }

}
