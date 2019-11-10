package application.wifi.zslsample;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.TextureView;

public class TextureListener implements TextureView.SurfaceTextureListener {
    private Activity mActivity;

    public TextureListener(Activity activity){
        mActivity = activity;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d("zslDemo", "onSurfaceTextureAvailable: ");
        CameraController.getInstance().openCamera(mActivity,width,height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
