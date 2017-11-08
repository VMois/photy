package com.example.a4ia1.photosmanager.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a4ia1.photosmanager.R;

/**
 * Created by vmois on 11/1/17.
 */

public class Miniature extends ImageView implements View.OnLongClickListener {
    private int width;
    private int height;
    private Activity activity;

    public Miniature(Context context, Bitmap bitmap, int width, int height, Activity activity) {
        super(context);
        this.width = width;
        this.height = height;
        this.activity = activity;
        bitmap = ImageTools.scaleBitmap(bitmap, width, height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        this.setImageBitmap(bitmap);
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
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        alert.show();
        return false;
    }
}
