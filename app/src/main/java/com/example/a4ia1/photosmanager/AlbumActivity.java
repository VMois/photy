package com.example.a4ia1.photosmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private File currentFolder;
    private ImageView deleteFolderButton;
    private List<File> picturesList;
    private LinearLayout imagesLayout;
    private LinearLayout.LayoutParams lparams;
    private LinearLayout childLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // hide top bar
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        String folderName = bundle.getString("folderName").toString();
        imagesLayout = (LinearLayout) findViewById(R.id.images_layout);
        picturesList = new ArrayList<>();

        // get display size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String mainFolderName = getString(R.string.main_folder_name);
        File mainDir = new File(pictureFolder, mainFolderName);
        currentFolder = new File(mainDir, "/" + folderName + "/");
        Log.d("Current Folder path", currentFolder.getAbsolutePath());

        if (currentFolder.isDirectory()) {
            // get all files
            // IMPORTANT! We don't check if file is image.
            // For learning purpose we know that all files in this folder is images
            for(File file: currentFolder.listFiles()) {
                if(file.isFile()) {
                    picturesList.add(file);
                }
            }
        }
        // initial childLayout set
        int baseHeight = size.y / 5;
        int small = size.x / 3;
        int big = (size.x / 3) * 2;
        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, baseHeight);
        childLayout = new LinearLayout(getApplicationContext());
        childLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLayout.setLayoutParams(lparams);

        int picturesCount = 1;
        // for properly image structure
        Boolean leftIsSmaller = true;
        for (File file: picturesList) {
            Bitmap betterImage = betterImageDecode(file.getAbsolutePath());
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(betterImage);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (leftIsSmaller) {
                iv.setLayoutParams(new LinearLayout.LayoutParams(small, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            } else {
                iv.setLayoutParams(new LinearLayout.LayoutParams(big, LinearLayout.LayoutParams.MATCH_PARENT, 2));
            }
            leftIsSmaller = !leftIsSmaller;
            childLayout.addView(iv);
            if (picturesCount % 2 == 0) {
                leftIsSmaller = !leftIsSmaller;
                imagesLayout.addView(childLayout);

                // create new layout to prevent bug
                childLayout = new LinearLayout(getApplicationContext());
                childLayout.setOrientation(LinearLayout.HORIZONTAL);
                childLayout.setLayoutParams(lparams);
            }
            picturesCount++;
        }

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

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }
}
