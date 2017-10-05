package com.example.a4ia1.photosmanager.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a4ia1.photosmanager.Helpers.CameraPreview;
import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    private CameraPreview cameraPreview;
    private FrameLayout cameraFrameLayout;
    private ImageView takePhotoButton;
    private ImageView savePhotoButton;
    private Button whiteBalanceButton;
    private Button picturesSizesButton;
    private Button supportedColorEffectsButton;
    private Button exposureCompensationButton;
    private Camera.Parameters camParams;
    private String[] whiteBalanceOptions;
    private String[] supportedColorEffects;
    private List<Camera.Size> picturesSizesOptions;
    private List<String> picturesSizesOptionsStrings;
    private List<String> exposureCompensationList;
    private byte[] photoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // hide top bar
        getSupportActionBar().hide();

        // init camera
        initCamera();
        initPreview();

        // get camera parameters
        camParams = camera.getParameters();
        whiteBalanceOptions = camParams.getSupportedWhiteBalance().toArray(new String[0]);
        picturesSizesOptions = camParams.getSupportedPictureSizes();
        supportedColorEffects = camParams.getSupportedColorEffects().toArray(new String[0]);
        int minExp = camParams.getMinExposureCompensation();
        int maxExp = camParams.getMaxExposureCompensation();
        exposureCompensationList = new ArrayList<>();
        for(int i = minExp; i <= maxExp; i++ ) {
            exposureCompensationList.add(String.valueOf(i));
        }

        picturesSizesOptionsStrings = new ArrayList<>();
        for(Camera.Size size: picturesSizesOptions) {
            String width = String.valueOf(size.width);
            String height = String.valueOf(size.height);
            picturesSizesOptionsStrings.add(width + "x" + height);
        }

        takePhotoButton = (ImageView) findViewById(R.id.take_photo_button);
        savePhotoButton = (ImageView) findViewById(R.id.save_photo_button);
        whiteBalanceButton = (Button) findViewById(R.id.white_balance_button);
        picturesSizesButton = (Button) findViewById(R.id.images_sizes_button);
        supportedColorEffectsButton = (Button) findViewById(R.id.color_effects_button);
        exposureCompensationButton = (Button) findViewById(R.id.exposure_compensation_button);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });
        savePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto(v);
            }
        });
        whiteBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteBalanceButtonClick(v);
            }
        });
        picturesSizesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturesSizesButtonClick(v);
            }
        });
        supportedColorEffectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportedColorEffectsButtonClick(v);
            }
        });
        exposureCompensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exposureCompensationButtonClick(v);
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

    private void savePhoto(View v) {
        Constants constants = new Constants();
        final File mainFolderFile = constants.getMainFolderFile();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String newPhotoName = dFormat.format(new Date());

        AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
        String title = getString(R.string.folder_choose);
        alert.setTitle(title);

        List<String> foldersList = new ArrayList<>();
        // get list of all folders in main folder
        for(File file: mainFolderFile.listFiles()) {
            if(file.isDirectory()) {
                foldersList.add(file.getName());
            }
        }
        final String[] folderArray = foldersList.toArray(new String[0]);
        alert.setItems(folderArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String pathToSave = mainFolderFile.getAbsolutePath()
                        + "/" + folderArray[which] + "/" + newPhotoName;
                savePhotoOnDisk(pathToSave);
            }
        });
        alert.show();
    }

    private void whiteBalanceButtonClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
        String title = getString(R.string.folder_choose);
        alert.setTitle(title);
        alert.setItems(whiteBalanceOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                camParams.setWhiteBalance(whiteBalanceOptions[which]);
                camera.setParameters(camParams);
            }
        });
        alert.show();
    }

    private void picturesSizesButtonClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
        String title = getString(R.string.folder_choose);
        alert.setTitle(title);
        alert.setItems(picturesSizesOptionsStrings.toArray(new String[0]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                camParams.setPictureSize(picturesSizesOptions.get(which).width, picturesSizesOptions.get(which).height);
                camera.setParameters(camParams);
            }
        });
        alert.show();
    }

    private void supportedColorEffectsButtonClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
        String title = getString(R.string.folder_choose);
        alert.setTitle(title);
        alert.setItems(supportedColorEffects, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                camParams.setColorEffect(supportedColorEffects[which]);
                camera.setParameters(camParams);
            }
        });
        alert.show();
    }

    private void exposureCompensationButtonClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
        String title = getString(R.string.folder_choose);
        alert.setTitle(title);
        alert.setItems(exposureCompensationList.toArray(new String[0]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                camParams.setExposureCompensation(Integer.parseInt(exposureCompensationList.get(which)));
                camera.setParameters(camParams);
            }
        });
        alert.show();
    }

    private void savePhotoOnDisk(String pathToSave) {
        try {
            FileOutputStream fs = new FileOutputStream(pathToSave);
            fs.write(photoData);
            fs.close();
            savePhotoButton.setVisibility(View.INVISIBLE);
        } catch (IOException err) {
            Log.e("[!] Bad photo save", err.toString());
        }
    }

    private Camera.PictureCallback camPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            photoData = data;
            // show button to save photo
            savePhotoButton.setVisibility(View.VISIBLE);
            camera.startPreview();
        }
    };
}