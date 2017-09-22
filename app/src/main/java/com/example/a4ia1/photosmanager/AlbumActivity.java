package com.example.a4ia1.photosmanager;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class AlbumActivity extends AppCompatActivity {

    private File currentFolder;
    private ImageView deleteFolderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Bundle bundle = getIntent().getExtras();
        String folderName = bundle.getString("folderName").toString();

        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String mainFolderName = getString(R.string.main_folder_name);
        File mainDir = new File(pictureFolder, mainFolderName);
        currentFolder = new File(mainDir, "/" + folderName + "/");
        Log.d("Current Folder path", currentFolder.getAbsolutePath());

        deleteFolderButton = (ImageView) findViewById(R.id.delete_folder_icon);

        deleteFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteFolderButtonClick(v);
            }
        });
    }

    private void onDeleteFolderButtonClick(View view) {
        String alertDeletedFolder = getString(R.string.alert_deleted_folder);
        String alertDeletedFolderNotExist = getString(R.string.alert_deleted_folder_not_exist);
        if(!currentFolder.exists()) {
            Toast.makeText(AlbumActivity.this, alertDeletedFolderNotExist, Toast.LENGTH_SHORT).show();
            return;
        }
        currentFolder.delete();
        Toast.makeText(AlbumActivity.this, alertDeletedFolder, Toast.LENGTH_SHORT).show();
        AlbumActivity.this.finish();
    }
}
