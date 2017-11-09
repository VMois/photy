package com.example.a4ia1.photosmanager.Activities;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.a4ia1.photosmanager.Helpers.CameraPreview;
import com.example.a4ia1.photosmanager.Helpers.Circle;
import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.ImageTools;
import com.example.a4ia1.photosmanager.Helpers.Miniature;
import com.example.a4ia1.photosmanager.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    private CameraPreview cameraPreview;
    private FrameLayout cameraFrameLayout;
    private ImageView takePhotoButton;
    // public ImageView savePhotoButton;
    private Spinner spinner;
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
    private Constants constants;

    private Point size;
    private int radius;
    private int centerX;
    private int centerY;
    private List<Miniature> miniatures;

    private OrientationEventListener orientationEventListener;
    private int angle = 0;
    private float startX;

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

        constants = new Constants();

        picturesSizesOptionsStrings = new ArrayList<>();
        for(Camera.Size size: picturesSizesOptions) {
            String width = String.valueOf(size.width);
            String height = String.valueOf(size.height);
            picturesSizesOptionsStrings.add(width + "x" + height);
        }

        takePhotoButton = (ImageView) findViewById(R.id.take_photo_button);
        // savePhotoButton = (ImageView) findViewById(R.id.save_photo_button);
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
        // For now is unused, could be deleted in future
        /*
        savePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto(v, photoData);
            }
        });
        */

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                CameraActivity.this,
                R.layout.spinner_row_layout,
                R.id.row_layout_text,
                Constants.SPINNER_OPTIONS);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        Log.d("Spinner Click", "Click on 0");
                        break;

                    // SAVE LAST
                    case 1:
                        if (miniatures.size() > 0) {
                            int tempId = miniatures.size() - 1;
                            Miniature temp = miniatures.get(tempId);
                            savePhoto(temp.getData(), tempId, false);
                            // also possibly to use, somethings like that
                            // savePhoto(photoData, miniatures.size() - 1);
                        }
                        break;

                    // SAVE ALL
                    case 2:
                        byte[] empty = {};
                        savePhoto(empty, -1, true);
                        break;

                    // DELETE ALL
                    case 3:
                        deleteAll();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        miniatures = new ArrayList<>();
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        centerX = size.x / 2;
        centerY = size.y / 2;
        radius = centerX / 2;
        Circle mainCircle = new Circle(getApplicationContext(), size, radius);
        cameraFrameLayout.addView(mainCircle);

        orientationEventListener = new OrientationEventListener(getApplicationContext()) {
            @Override
            public void onOrientationChanged(int o_angle) {
                if (o_angle >= 0 && o_angle <= 63) angle = 0;
                if (o_angle > 70 && o_angle <= 180) angle = -90;
                if (o_angle > 185 && o_angle <= 298) angle = 90;
                if (o_angle > 305 && o_angle <= 360) angle = 0;

                int animationSpeed = 100;
                ObjectAnimator.ofFloat(takePhotoButton, View.ROTATION, angle)
                        .setDuration(animationSpeed)
                        .start();
                ObjectAnimator.ofFloat(whiteBalanceButton, View.ROTATION, angle)
                        .setDuration(animationSpeed)
                        .start();
                ObjectAnimator.ofFloat(picturesSizesButton, View.ROTATION, angle)
                        .setDuration(animationSpeed)
                        .start();
                ObjectAnimator.ofFloat(supportedColorEffectsButton, View.ROTATION, angle)
                        .setDuration(animationSpeed)
                        .start();
                ObjectAnimator.ofFloat(exposureCompensationButton, View.ROTATION, angle)
                        .setDuration(animationSpeed)
                        .start();
                for (int i = 0; i < miniatures.size(); i++) {
                    ObjectAnimator.ofFloat(miniatures.get(i), View.ROTATION, angle)
                            .setDuration(animationSpeed)
                            .start();
                }
            }
        };
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
        orientationEventListener.disable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            initCamera();
            initPreview();
        }
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        } else {
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

    public void savePhoto(final byte[] data, final int id, final boolean saveAll) {
        File mainFolderFile = constants.getMainFolderFile();

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
                if (!saveAll) {
                    saveImage(folderArray[which], data, id);
                    removeMiniature(id);
                } else {
                    for (Miniature temp : miniatures) {
                        saveImage(folderArray[which], temp.getData(), temp.getId());
                    }
                    deleteAll();
                }
            }
        });
        alert.show();
    }

    private void saveImage(String folderToSave, byte[] data, int id) {
        Random rand = new Random();

        File mainFolderFile = constants.getMainFolderFile();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        int randomSuffix = rand.nextInt(1000) + 100;
        String newPhotoName = dFormat.format(new Date()) + "" + randomSuffix;

        String pathToSave = mainFolderFile.getAbsolutePath()
                + "/" + folderToSave + "/" + newPhotoName;
        ImageTools.saveOnDisk(pathToSave, data);
    }

    private void deleteAll() {
        for (int k = 0; k < miniatures.size(); k++) {
            cameraFrameLayout.removeView(miniatures.get(k));
        }
        miniatures = new ArrayList<>();
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

    private Camera.PictureCallback camPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            photoData = data;

            Miniature min = new Miniature(getApplicationContext(),
                    data,
                    radius / 2,
                    radius / 2,
                    CameraActivity.this);
            min.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    float x = motionEvent.getRawX();

                    switch(motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = view.getX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            view.setX(x);

                            // what distance should I swipe to delete miniature
                            double test = radius * 1.5;
                            if (startX > size.x / 2) {
                                test = radius;
                            }

                            if (Math.abs(startX - x) > test) {
                                Miniature temp = (Miniature) view;
                                removeMiniature(temp.getMiniatureId());
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            view.setX(startX);
                            break;
                    }
                    return false;
                }
            });
            miniatures.add(min);

            reDrawMiniatures();

            // disable savePhotoButton for now
            // show button to save photo
            // savePhotoButton.setVisibility(View.VISIBLE);

            camera.startPreview();
        }
    };

    public void removeMiniature(int id) {
        cameraFrameLayout.removeView(miniatures.get(id));
        miniatures.remove(id);
        reDrawMiniatures();
    }

    private void reDrawMiniatures() {
        // remove all miniatures
        for (int i = 0; i < miniatures.size(); i++) {
            cameraFrameLayout.removeView(miniatures.get(i));
        }

        // set alpha
        double a = 0;

        // set default alpha step for this count of miniatures
        double step = 0;
        if (miniatures.size() > 0) {
            step = 360 / miniatures.size();
        }

        // add miniatures
        for (int i = 0; i < miniatures.size(); i++) {
            double y = radius * Math.sin(Math.toRadians(a));
            double x = radius * Math.cos(Math.toRadians(a));
            Miniature temp = miniatures.get(i);

            // set id from miniatures list
            temp.setId(i);

            temp.setX(centerX - (float) x - radius / 4);
            temp.setY(centerY - (float) y - radius / 4);

            cameraFrameLayout.addView(temp);

            // apply step to alpha
            a += step;
        }
    }

    public void showMiniaturePreview(Miniature min) {
        ImageView im = new ImageView(getApplicationContext());
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(min.getData(), 0, min.getData().length);
        originalBitmap = ImageTools.rotate(originalBitmap, -90);
        originalBitmap = Bitmap.createScaledBitmap(originalBitmap, (int)(size.x * 0.8), (int)(size.y * 0.8), true);
        im.setImageBitmap(originalBitmap);
        // im.setScaleType(ImageView.ScaleType.CENTER_CROP);
        im.setX((float)(size.x * 0.05));
        im.setY((float)(size.y * 0.05));
        im.bringToFront();
        whiteBalanceButton.setVisibility(View.INVISIBLE);
        picturesSizesButton.setVisibility(View.INVISIBLE);
        supportedColorEffectsButton.setVisibility(View.INVISIBLE);
        exposureCompensationButton.setVisibility(View.INVISIBLE);
        takePhotoButton.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFrameLayout.removeView(view);
                whiteBalanceButton.setVisibility(View.VISIBLE);
                picturesSizesButton.setVisibility(View.VISIBLE);
                supportedColorEffectsButton.setVisibility(View.VISIBLE);
                exposureCompensationButton.setVisibility(View.VISIBLE);
                takePhotoButton.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
            }
        });
        cameraFrameLayout.addView(im);
    }
}
