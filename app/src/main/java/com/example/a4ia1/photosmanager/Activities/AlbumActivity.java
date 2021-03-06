package com.example.a4ia1.photosmanager.Activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.CustomImageView;
import com.example.a4ia1.photosmanager.Helpers.DatabaseManager;
import com.example.a4ia1.photosmanager.R;

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
    private Point size;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // hide top bar
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        String folderName = bundle.getString("folderName").toString();
        imagesLayout = (LinearLayout) findViewById(R.id.images_layout);

        // get display size
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        Constants constants = new Constants();
        File mainDir = constants.getMainFolderFile();
        currentFolder = new File(mainDir, "/" + folderName + "/");

        deleteFolderButton = (ImageView) findViewById(R.id.delete_folder_icon);

        deleteFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteFolderButtonClick(v);
            }
        });

        db = new DatabaseManager(
                AlbumActivity.this,
                Constants.MAIN_DATABASE_NAME,
                null,
                1
        );
        updateImagesList();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        updateImagesList();
    }

    private void onDeleteFolderButtonClick(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AlbumActivity.this);
        alert.setTitle("Do you want to delete " + currentFolder.getName() + "?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String alertDeletedFolder = getString(R.string.alert_deleted_folder);
                String alertDeletedFolderNotExist = getString(R.string.alert_deleted_folder_not_exist);
                if(!currentFolder.exists()) {
                    Toast.makeText(AlbumActivity.this, alertDeletedFolderNotExist, Toast.LENGTH_SHORT).show();
                    return;
                }
                File[] currentFiles = currentFolder.listFiles();
                if (currentFiles.length > 0) {
                    for(File file: currentFiles) {
                        file.delete();
                    }
                }
                currentFolder.delete();
                Toast.makeText(AlbumActivity.this, alertDeletedFolder, Toast.LENGTH_SHORT).show();
                AlbumActivity.this.finish();
            }

        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    private void updateImagesList() {
        picturesList = new ArrayList<>();
        imagesLayout.removeAllViews();
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
        int listSize = picturesList.size();
        // for properly image structure
        Boolean leftIsSmaller = true;
        for (File file: picturesList) {
            Bitmap betterImage = betterImageDecode(file.getAbsolutePath());
            CustomImageView iv = new CustomImageView(AlbumActivity.this, file, this.db);
            if (leftIsSmaller) {
                betterImage = Bitmap.createScaledBitmap(betterImage, small, betterImage.getHeight(), true);
                iv.setLayoutParams(new LinearLayout.LayoutParams(small, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            } else {
                betterImage = Bitmap.createScaledBitmap(betterImage, big, betterImage.getHeight(), true);
                iv.setLayoutParams(new LinearLayout.LayoutParams(big, LinearLayout.LayoutParams.MATCH_PARENT, 2));
            }
            iv.setImageBitmap(betterImage);
            iv.setId(picturesCount - 1);
            leftIsSmaller = !leftIsSmaller;
            if (listSize == picturesCount && picturesCount % 2 != 0) {
                iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                childLayout.addView(iv);
                imagesLayout.addView(childLayout);
                break;
            }
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
    }
}
