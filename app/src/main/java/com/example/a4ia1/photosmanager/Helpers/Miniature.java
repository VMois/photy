package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by vmois on 11/1/17.
 */

public class Miniature extends ImageView {
    private int width;
    private int height;

    public Miniature(Context context, Bitmap bitmap, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        bitmap = ImageTools.scaleBitmap(bitmap, width, height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        this.setImageBitmap(bitmap);
        this.setLayoutParams(params);
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
}
