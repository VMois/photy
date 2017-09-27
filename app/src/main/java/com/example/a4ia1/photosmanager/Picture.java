package com.example.a4ia1.photosmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

public class Picture extends AppCompatActivity {

    private ImageView mainImage;
    private ImageView scaleIconButton;
    private Bitmap imageBitmap;
    // 0 - big, 1 - middle, 2 - small;
    private int scaleTypes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // hide top bar
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        String imagePath = bundle.getString("imagePath").toString();
        imageBitmap = betterImageDecode(imagePath);

        mainImage = (ImageView) findViewById(R.id.main_image);
        mainImage.setImageBitmap(imageBitmap);

        scaleIconButton = (ImageView) findViewById(R.id.scale_icon);
        scaleIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScaleButtonClick(v);
            }
        });
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    private void onScaleButtonClick(View view) {

    }
}
