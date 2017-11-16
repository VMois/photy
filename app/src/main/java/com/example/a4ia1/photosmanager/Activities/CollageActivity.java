package com.example.a4ia1.photosmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.ImageData;
import com.example.a4ia1.photosmanager.Helpers.ImageTools;
import com.example.a4ia1.photosmanager.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CollageActivity extends AppCompatActivity {

    private List<ImageData> collageList;
    private FrameLayout frameLayout;
    private ImageView lastImageView;
    private Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        // hide top bar
        getSupportActionBar().hide();

        frameLayout = (FrameLayout) findViewById(R.id.collage_frame_layout);
        frameLayout.setDrawingCacheEnabled(true);
        collageList = (ArrayList<ImageData>) getIntent().getExtras().getSerializable("collage");
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        // 1% percent of width and height
        float stepWidth = (float) size.x / 100;
        float stepHeight = (float) size.y / 100;

        for (ImageData ivData : collageList) {
            ImageView temp = new ImageView(getApplicationContext());
            float xMove = ivData.getX() * stepWidth;
            float yMove = ivData.getY() * stepHeight;
            temp.setX(xMove);
            temp.setY(yMove);
            temp.setImageResource(R.drawable.take_photo);
            temp.setBackgroundResource(R.drawable.image_border);
            int wSize = (int)(ivData.getW() * stepWidth);
            int hSize = (int)(ivData.getH() * stepHeight);
            temp.setLayoutParams(new FrameLayout.LayoutParams(wSize, hSize));
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastImageView = (ImageView) view;
                    AlertDialog.Builder alert = new AlertDialog.Builder(CollageActivity.this);
                    String collageTitle = getString(R.string.collage_photo_take);
                    alert.setTitle(collageTitle);
                    alert.setItems(Constants.COLLAGE_PHOTO_DIALOG_OPTIONS, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            handlePicturesOptions(which);
                        }
                    });
                    alert.show();
                }
            });
            temp.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(CollageActivity.this);
                    String collageTitle = getString(R.string.collage_options_title);
                    alert.setTitle(collageTitle);
                    alert.setItems(Constants.COLLAGE_DIALOG_OPTIONS, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 0 - save collage on disk
                            switch(which) {
                                case 0:
                                    Constants constants = new Constants();
                                    File mainFolder = constants.getMainFolderFile();
                                    File folderToWrite = new File(mainFolder, Constants.FOLDER_NAME_FOR_COLLAGES);
                                    if (!folderToWrite.exists()) {
                                        folderToWrite.mkdir();
                                    }
                                    Bitmap collageBitmap = frameLayout.getDrawingCache(true);
                                    ImageTools.saveBitmapOnDisk(folderToWrite.getAbsolutePath(), collageBitmap);
                                    return;
                            }
                        }
                    });
                    alert.show();
                    return false;
                }
            });
            frameLayout.addView(temp);
        }
    }

    public void handlePicturesOptions(int which) {
        // gallery - 0
        // camera - 1
        switch (which) {
            case 0:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 100);
                break;
            case 1:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 200);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 100 - gallery,
        // 200 - camera
        if (resultCode == -1) {
            switch (requestCode) {
                case 100:
                    Uri imgData = data.getData();
                    try {
                        InputStream stream = getContentResolver().openInputStream(imgData);
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        lastImageView.setImageBitmap(bitmap);
                        lastImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 200:
                    Bundle extras = data.getExtras();
                    Bitmap b = (Bitmap) extras.get("data");
                    lastImageView.setImageBitmap(b);
                    lastImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
            }
        }
    }
}
