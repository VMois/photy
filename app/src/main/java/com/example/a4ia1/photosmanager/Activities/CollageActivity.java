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
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.ImageData;
import com.example.a4ia1.photosmanager.Helpers.ImageTools;
import com.example.a4ia1.photosmanager.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

        frameLayout = (FrameLayout) findViewById(R.id.collage_frame_layout);
        frameLayout.setDrawingCacheEnabled(true);
        collageList = (ArrayList<ImageData>) getIntent().getExtras().getSerializable("collage");
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        // 1% percent of width and height
        float stepWidth = size.x / 100;
        float stepHeight = size.y / 100;

        // second options to get list
        // collageList = (ArrayList<ImageData>) getIntent().getSerializableExtra("collage");
        for (ImageData ivData : collageList) {
            ImageView temp = new ImageView(getApplicationContext());
            temp.setX(ivData.getX() * stepWidth);
            temp.setY(ivData.getY() * stepHeight);
            temp.setImageResource(R.drawable.collage);
            temp.setScaleType(ImageView.ScaleType.CENTER_CROP);
            temp.setLayoutParams(new FrameLayout.LayoutParams(ivData.getW() * (int) stepWidth, ivData.getH() * (int) stepHeight));
            // temp.setLayoutParams(new FrameLayout.LayoutParams(ivData.getW(), ivData.getH()));
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    lastImageView = (ImageView) view;
                    AlertDialog.Builder alert = new AlertDialog.Builder(CollageActivity.this);
                    String collageTitle = getString(R.string.collage_photo_take);
                    alert.setTitle(collageTitle);
                    alert.setItems(Constants.COLLAGE_DIALOG_OPTIONS, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            handlePicturesOptions(which, view);
                        }
                    });
                    alert.show();
                }
            });
            frameLayout.addView(temp);
        }
    }

    public void handlePicturesOptions(int which, View view) {
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
        switch (requestCode) {
            case 100:
                Uri imgData = data.getData();
                try {
                    InputStream stream = getContentResolver().openInputStream(imgData);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    bitmap = ImageTools.defaultScale(bitmap);
                    lastImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                   e.printStackTrace();
                }
                break;
            case 200:
                Bundle extras = data.getExtras();
                Bitmap b = (Bitmap) extras.get("data");
                b = ImageTools.defaultScale(b);
                lastImageView.setImageBitmap(b);
                break;
        }
    }
}
