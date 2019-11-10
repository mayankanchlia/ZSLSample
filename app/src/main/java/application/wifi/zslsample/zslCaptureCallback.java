package application.wifi.zslsample;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;

public class zslCaptureCallback extends CameraCaptureSession.CaptureCallback {

    @Override
    public void onCaptureProgressed( CameraCaptureSession session,
                                     CaptureRequest request,
                                     CaptureResult partialResult) {
        Log.d("zslDemo", "onCaptureProgressed: ");
//        process(partialResult);
    }

    @Override
    public void onCaptureCompleted( CameraCaptureSession session,
                                    CaptureRequest request,
                                    TotalCaptureResult result) {
        Log.d("zslDemo", "onCaptureCompleted: ");
//        process(result);
    }
}
