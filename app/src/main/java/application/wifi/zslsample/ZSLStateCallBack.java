package application.wifi.zslsample;

import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;
import android.util.Log;

public class ZSLStateCallBack extends CameraDevice.StateCallback {
    String TAG= "ZSLSample";
    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        Log.d(TAG, "onOpened: ");
        //TODO implement camera Lock
        CameraParameters.getInstance().cameraDevice = camera;
        CameraController.getInstance().createPreviewSession();
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {

    }

    @Override
    public void onError(@NonNull CameraDevice camera, int error) {

    }
}
