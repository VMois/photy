package com.example.a4ia1.photosmanager;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    private CameraPreview cameraPreview;
    private FrameLayout cameraFrameLayout;
    private ImageView takePhotoButton;
    private byte[] photoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // hide top bar
        getSupportActionBar().hide();

        initCamera();
        initPreview();

        takePhotoButton = (ImageView) findViewById(R.id.take_photo_button);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            // important line, not documented in API
            cameraPreview.getHolder().removeCallback(cameraPreview);
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            initCamera();
            initPreview();
        }
    }

    private int getCameraId() {
        int cid = 0;
        int camerasCount = Camera.getNumberOfCameras();

        for (int i = 0; i < camerasCount; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            /* if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cid = i;
            }*/
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cid = i;
            }
        }

        return cid;
    }

    private void initCamera() {
        boolean cam = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!cam) {
            // no camera
        } else {
            cameraId = getCameraId();
            if (cameraId < 0) {
            } else {
                camera = Camera.open(cameraId);
            }
        }
    }

    private void initPreview() {
        cameraPreview = new CameraPreview(CameraActivity.this, camera);
        cameraFrameLayout = (FrameLayout) findViewById(R.id.camera_frame_layout);
        cameraFrameLayout.addView(cameraPreview);
    }

    private void takePhoto(View v) {
        camera.takePicture(null, null, camPictureCallback);
    }

    private void savePhotoOnDisk() {
        Constants constants = new Constants();
        constants.getMainFolderFile();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String d = dFormat.format(new Date());
        try {
            FileOutputStream fs = new FileOutputStream(constants.getMainFolderFile().getAbsolutePath() + "/places/" + d + ".png");
            fs.write(photoData);
            fs.close();
        } catch (IOException err) {
            Log.e("[!] Bad photo save", err.toString());
        }
    }

    private Camera.PictureCallback camPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            photoData = data;
            savePhotoOnDisk();
            camera.startPreview();
            // savePhotoOnDisk();

        }
    };
}
