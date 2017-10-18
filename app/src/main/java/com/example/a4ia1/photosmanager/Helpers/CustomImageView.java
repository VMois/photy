package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.example.a4ia1.photosmanager.Activities.Picture;

import java.io.File;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class CustomImageView extends ImageView implements ImageView.OnClickListener {

    private File currentImage;
    private Context currentContext;

    public CustomImageView(Context context, File CurrentImage, Context CurrentContext) {
        super(context);
        this.currentImage = CurrentImage;
        this.currentContext = CurrentContext;
        this.setScaleType(ScaleType.CENTER_CROP);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String imagePath = currentImage.getAbsolutePath();
        Intent intent = new Intent(currentContext, Picture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("imagePath", imagePath);
        currentContext.startActivity(intent);
    }
}
