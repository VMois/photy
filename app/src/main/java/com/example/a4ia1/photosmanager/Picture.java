package com.example.a4ia1.photosmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class Picture extends AppCompatActivity {

    private ImageView mainImage;
    private Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // hide top bar
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        String imagePath = bundle.getString("imagePath").toString();
        Log.d("Img path: ", imagePath);
        imageBitmap = betterImageDecode(imagePath);

        mainImage = (ImageView) findViewById(R.id.main_image);
        mainImage.setImageBitmap(imageBitmap);
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }
}
