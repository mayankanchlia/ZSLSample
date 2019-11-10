package application.wifi.zslsample;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class CameraActivity extends AppCompatActivity {
    private String TAG = CameraActivity.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.zsl_sample_activity);
        if(null == savedInstanceState) {
            Log.d(TAG, "onCreate: ");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ZslCameraFragment.newInstance())
                    .commit();
        }
    }
}
