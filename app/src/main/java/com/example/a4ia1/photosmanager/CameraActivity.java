package com.example.a4ia1.photosmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.widget.FrameLayout;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    // private CameraPreview _cameraPreview;
    private FrameLayout cameraFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // hide top bar
        getSupportActionBar().hide();
    }
}
