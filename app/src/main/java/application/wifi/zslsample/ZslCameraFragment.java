package application.wifi.zslsample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

public class ZslCameraFragment extends Fragment {
    private TextureView mTextureView;
    private String TAG = ZslCameraFragment.class.getSimpleName();

    public static ZslCameraFragment newInstance(){
        return new ZslCameraFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.zsl_camera_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextureView= view.findViewById(R.id.texture_foreground);
        mTextureView.setSurfaceTextureListener(new TextureListener(getActivity()));
        CameraParameters.getInstance().textureView = mTextureView;
        //TODO Logic for CameraID
        CameraParameters.getInstance().cameraID = "0";
        Log.d(TAG, "onViewCreated: ");

    }
}
