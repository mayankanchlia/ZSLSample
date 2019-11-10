package application.wifi.zslsample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CameraController {
    private Activity mActivity;
    private String TAG = CameraController.class.getSimpleName();
    private static CameraController mCameraController;

    public static CameraController getInstance() {
        if (mCameraController == null) {
            mCameraController = new CameraController();
        }
        return mCameraController;
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    private ImageReader mJpegImageReader, mRawImageReader;
    private ImageReader.OnImageAvailableListener mJpegImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {

        }
    };
    private ImageReader.OnImageAvailableListener mRawImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                image.close();

        }
    };
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private CameraParameters mParams;
    private void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("Camera Backgorund");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    public void openCamera(Activity activity, int width, int height) {
        Log.d("zslDemo", "openCamera: ");
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startBackgroundThread();
        mParams = CameraParameters.getInstance();
        mActivity = activity;
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (Arrays.asList(manager.getCameraIdList()).contains(mParams.cameraID)) {
                mParams.cameraCharacteristics = manager.getCameraCharacteristics(mParams.cameraID);
                StreamConfigurationMap map = mParams.cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mParams.pictureSize = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                mParams.jpegImageReader = ImageReader.newInstance(mParams.pictureSize.getHeight(),
                        mParams.pictureSize.getWidth(), ImageFormat.JPEG, 2);
                mParams.rawImageReader = ImageReader.newInstance(mParams.pictureSize.getWidth(),
                        mParams.pictureSize.getWidth(), ImageFormat.PRIVATE, 11);
                mParams.jpegImageReader.setOnImageAvailableListener(mJpegImageAvailableListener, mBackgroundHandler);
                mParams.rawImageReader.setOnImageAvailableListener(mRawImageAvailableListener, mBackgroundHandler);
                mParams.previewSize = new Size(1920, 1080);
                int[] camCapabilities = mParams.cameraCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
                for (int capability : camCapabilities) {
                    if (capability == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_PRIVATE_REPROCESSING) {
                        mParams.canReprocess = true;
                    }
                }
                if (!mParams.canReprocess) {
                    showToast("Reprocess not supported");
                }
                manager.openCamera(mParams.cameraID, new ZSLStateCallBack(), mBackgroundHandler);

                // TODO check orientations and display rotation


            }else{

            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void createPreviewSession(){
        Log.d("zslDemo", "createPreviewSession: ");
        SurfaceTexture texture = mParams.textureView.getSurfaceTexture();
        assert texture!= null;
        texture.setDefaultBufferSize(mParams.pictureSize.getWidth(), mParams.pictureSize.getHeight());
        Surface surface = new Surface(texture);
        Surface rawSurface = mParams.rawImageReader.getSurface();
        Surface jpegSurface = mParams.jpegImageReader.getSurface();
        try {
            mParams.previewRequestBuilder = mParams.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
            mParams.previewRequestBuilder.addTarget(surface);
            if(rawSurface!= null){
                mParams.previewRequestBuilder.addTarget(rawSurface);
            }
            if(mParams.canReprocess){
                mParams.cameraDevice.createReprocessableCaptureSession(new InputConfiguration(mParams.pictureSize.getWidth(),
                                mParams.pictureSize.getHeight(), ImageFormat.PRIVATE),
                        Arrays.asList(surface, rawSurface, jpegSurface),
                        new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(@NonNull CameraCaptureSession session) {
                                Log.d("zslDemo", "onConfigured: ");
                                    if(mParams.cameraDevice == null){
                                        return;
                                    }
                                    mParams.captureSession = session;
                                    mParams.previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                try {
                                    mParams.captureSession.setRepeatingRequest(mParams.previewRequestBuilder.build(),
                                            new zslCaptureCallback(),mBackgroundHandler);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                            }
                        },mBackgroundHandler);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void showToast(final String string){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, string, Toast.LENGTH_LONG).show();
            }
        });
    }
}
