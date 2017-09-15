package com.example.a4ia1.photosmanager;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private LinearLayout albumsButton;
    private LinearLayout cameraButton;
    private ImageButton  leftArrowButton;
    private ImageButton  rightArrowButton;
    private LinearLayout collageButton;
    private LinearLayout networkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity Create", "Start main Activity");

        // test if folder exist
        Log.i("MainActivity Create", "Test main folder");
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.i("MainActivity Create", "Path: " + pictureFolder.getAbsolutePath());
        String mainFolderName = getString(R.string.main_folder_name);
        File mainDir = new File(pictureFolder, mainFolderName);

        if (!mainDir.exists()) {
            Log.i("MainActivity Create", "Folder doesn't exist, create");
            mainDir.mkdir();
        }

        // set click event for AlbumsButton
        albumsButton = (LinearLayout) findViewById(R.id.albums_button);
        leftArrowButton = (ImageButton) findViewById(R.id.left_arrow);
        rightArrowButton = (ImageButton) findViewById(R.id.right_arrow);
        cameraButton = (LinearLayout) findViewById(R.id.camera_button);
        collageButton = (LinearLayout) findViewById(R.id.collage_button);
        networkButton = (LinearLayout) findViewById(R.id.network_button);

        albumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlbumsButtonClick(v);
            }
        });

    }

    // event function for AlbumsButton
    private void onAlbumsButtonClick(View view) {
        Log.i("AlbumsButton Click", "Start Albums Activity");
        Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
        startActivity(intent);
    }
}
