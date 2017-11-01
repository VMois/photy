package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

/**
 * Created by 4ia1 on 2017-10-26.
 */
public class Circle extends View {
    private Point size;
    private int radius;
    public Circle(Context context, Point size, int radius) {
        super(context);
        this.size = size;
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        canvas.drawCircle(size.x / 2, size.y / 2, radius, paint);
    }
}
