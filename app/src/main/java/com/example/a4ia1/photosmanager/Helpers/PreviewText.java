package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by vmois on 11/29/17.
 */

public class PreviewText extends View {
    private Paint paint;
    private String text;
    private float x;
    private float y;

    public PreviewText(Context context, String text, Typeface tf, float X, float Y) {
        super(context);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(100);
        paint.setTypeface(tf);
        this.text = text;
        this.x = X;
        this.y = Y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawText(text, x, y, paint);
    }
}
