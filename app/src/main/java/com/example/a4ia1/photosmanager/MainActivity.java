package com.example.a4ia1.photosmanager;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout  albumsButton;
    private LinearLayout  cameraButton;
    private Button     leftArrowButton;
    private Button    rightArrowButton;
    private LinearLayout collageButton;
    private LinearLayout networkButton;
    private List<String> foldersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // consts
        String[] baseSubFolders = new String[]{ "places", "people", "things" };
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String mainFolderName = getString(R.string.main_folder_name);
        File mainDir = new File(pictureFolder, mainFolderName);
        foldersList = new ArrayList<>();

        // check if main and base sub folders exist
        // if not create them
        // START
        if (!mainDir.exists()) {
            mainDir.mkdir();
        }

        if(mainDir.isDirectory()) {
            for (String folderName : baseSubFolders) {
                File tmpFolder = new File(mainDir, folderName);
                if (!tmpFolder.exists()) {
                    tmpFolder.mkdir();
                }
            }
        }
        // END

        // set click event for AlbumsButton
        albumsButton = (LinearLayout) findViewById(R.id.albums_button);
        leftArrowButton = (Button) findViewById(R.id.left_arrow);
        rightArrowButton = (Button) findViewById(R.id.right_arrow);
        cameraButton = (LinearLayout) findViewById(R.id.camera_button);
        collageButton = (LinearLayout) findViewById(R.id.collage_button);
        networkButton = (LinearLayout) findViewById(R.id.network_button);

        albumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlbumsButtonClick(v);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraButtonClick(v);
            }
        });

    }

    // event function for AlbumsButton
    private void onAlbumsButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
        Bundle arrayBundle = new Bundle();
        startActivity(intent);
    }

    private void onCameraButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        Bundle arrayBundle = new Bundle();
        startActivity(intent);
    }
}
