public class Camera {
    int camera;
    int cameraRFID;
    int status;
    Camera(int cameraNumber, int id){
        camera = cameraNumber;
        cameraRFID = id;
        status = 0;
    }

    public int getCamera() {
        return camera;
    }

    public int getCameraID() {
        return cameraRFID;
    }

    public int getStatus() {
        return status;
    }

    public void setCameraRFID(int cameraRFID) {
        this.cameraRFID = cameraRFID;
    }

    public void setCamera(int camera) {
        this.camera = camera;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
