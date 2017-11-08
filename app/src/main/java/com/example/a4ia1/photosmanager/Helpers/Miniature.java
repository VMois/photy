package com.example.a4ia1.photosmanager.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a4ia1.photosmanager.Activities.CameraActivity;
import com.example.a4ia1.photosmanager.R;

/**
 * Created by vmois on 11/1/17.
 */

public class Miniature extends ImageView implements View.OnLongClickListener {
    private int width;
    private int height;
    private Activity activity;
    private byte[] data;
    private int id = 0;
    private CameraActivity cameraActivity;

    public Miniature(Context context, byte[] data, int width, int height, Activity activity) {
        super(context);
        this.width = width;
        this.height = height;
        this.activity = activity;
        this.data = data;
        this.cameraActivity = (CameraActivity) activity;

        // create bitmap from byte[] data
        Bitmap convertedBitmap = ImageTools.fromByteToBitmap(data);

        // set proper rotation
        convertedBitmap = ImageTools.rotate(convertedBitmap, -90);

        // set bitmap scale
        convertedBitmap = ImageTools.scaleBitmap(convertedBitmap, width, height);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        this.setImageBitmap(convertedBitmap);
        this.setLayoutParams(params);

        setOnLongClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rect rec = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.GREEN);
        canvas.drawRect(rec, paint);
    }

    @Override
    public boolean onLongClick(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        String alertTitle = view.getResources().getString(R.string.miniature_options_alert_title);
        alert.setTitle(alertTitle);
        alert.setItems(Constants.MINIATURES_OPTIONS, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // Show
                    case 0:
                        break;
                    // Delete
                    case 1:
                        cameraActivity.removeMiniature(id);
                        break;
                    // Save
                    case 2:
                        cameraActivity.savePhoto(data, id);
                        break;
                }
            }
        });
        alert.show();
        return false;
    }

    public void setId(int i) {
        this.id = i;
    }

    public byte[] getData() {
        return this.data;
    }
}
