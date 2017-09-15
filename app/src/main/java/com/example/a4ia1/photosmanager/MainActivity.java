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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout albumsButton;
    private LinearLayout cameraButton;
    private ImageButton  leftArrowButton;
    private ImageButton  rightArrowButton;
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

        // folders and files process
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

            // get list of all folders in main folder
            for(File file: mainDir.listFiles()) {
                if(file.isDirectory()) {
                    foldersList.add(file.getName());
                }
            }
        }
        // END

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
        Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
        Bundle arrayBundle = new Bundle();
        arrayBundle.putStringArray("folders", foldersList.toArray(new String[0]));
        intent.putExtras(arrayBundle);
        startActivity(intent);
    }
}
