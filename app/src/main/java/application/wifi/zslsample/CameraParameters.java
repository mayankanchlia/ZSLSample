package application.wifi.zslsample;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.util.Size;
import android.view.TextureView;

import java.util.logging.Handler;

public class CameraParameters {
   public  String cameraID;
   public  Size pictureSize;
   public  CameraDevice cameraDevice;
   public  CameraCharacteristics cameraCharacteristics;
   public ImageReader jpegImageReader, rawImageReader;
   public Size previewSize;
   public boolean canReprocess;
   public TextureView textureView;
   public CaptureRequest.Builder previewRequestBuilder;
   public CameraCaptureSession captureSession;
   private static CameraParameters mCameraParameters;
   public static CameraParameters getInstance(){
       if(mCameraParameters == null){
           mCameraParameters = new CameraParameters();
       }
       return mCameraParameters;
   }
}
