package com.example.a4ia1.photosmanager.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a4ia1.photosmanager.R;

import java.io.File;

public class Picture extends AppCompatActivity {

    private ImageView mainImage;
    private ImageView scaleIconButton;
    private ImageView deleteButton;
    private File currentFile;
    private Bitmap originalBitmap;
    private Bitmap imageBitmap;
    private int originalWidth;
    private int originalHeight;
    private Point size;

    private RelativeLayout.LayoutParams params;
    // 0 - big, 1 - middle, 2 - small;
    private int scaleType = 1;
    final private int bigParams = RelativeLayout.LayoutParams.MATCH_PARENT;
    final private int middleParams = RelativeLayout.LayoutParams.WRAP_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // hide top bar
        getSupportActionBar().hide();

        // get display sizes
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        Bundle bundle = getIntent().getExtras();
        String imagePath = bundle.getString("imagePath").toString();
        currentFile = new File(imagePath);
        originalBitmap = betterImageDecode(imagePath);
        originalWidth = originalBitmap.getWidth();
        originalHeight = originalBitmap.getHeight();
        imageBitmap = Bitmap.createScaledBitmap(originalBitmap, size.x, size.y, true);

        mainImage = (ImageView) findViewById(R.id.main_image);
        mainImage.setImageBitmap(imageBitmap);

        scaleIconButton = (ImageView) findViewById(R.id.scale_icon);
        scaleIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScaleButtonClick(v);
            }
        });

        deleteButton = (ImageView) findViewById(R.id.delete_image_icon);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteImageButtonClick(v);
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
        // params = new RelativeLayout.LayoutParams(bigParams, bigParams);
        switch (scaleType) {
            case 0:
                // params = new RelativeLayout.LayoutParams(bigParams, bigParams);
                imageBitmap = Bitmap.createScaledBitmap(originalBitmap, size.x, size.y, true);
                break;
            case 1:
                imageBitmap = Bitmap.createScaledBitmap(originalBitmap, originalWidth, originalWidth, true);
                // params = new RelativeLayout.LayoutParams(middleParams, middleParams);
                break;
            case 2:
                // params = new RelativeLayout.LayoutParams(middleParams, middleParams);
                imageBitmap = Bitmap.createScaledBitmap(originalBitmap, (int)(mainImage.getWidth()*0.5), (int)(mainImage.getHeight()*0.5), true);
                break;
            default:
                break;
        }
        // mainImage.setLayoutParams(params);
        mainImage.setImageBitmap(imageBitmap);
        scaleType++;
        if (scaleType > 2) {
            scaleType = 0;
        }
    }

    private void onDeleteImageButtonClick(View view) {
        String alertDeletedFolder = getString(R.string.alert_deleted_folder);
        String alertDeletedFolderNotExist = getString(R.string.alert_deleted_folder_not_exist);
        if(!currentFile.exists()) {
            Toast.makeText(getApplicationContext(), alertDeletedFolderNotExist, Toast.LENGTH_SHORT).show();
            return;
        }
        currentFile.delete();
        Toast.makeText(getApplicationContext(), alertDeletedFolder, Toast.LENGTH_SHORT).show();
        Picture.this.finish();
    }
}
