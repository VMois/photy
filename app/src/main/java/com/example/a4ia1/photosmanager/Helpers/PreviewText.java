package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

/**
 * Created by vmois on 11/29/17.
 */

public class PreviewText extends View implements View.OnClickListener {
    private Paint paint;
    private String text;
    private Canvas currentCanvas;
    private float x;
    private float y;
    private float textWidth;
    private float textHeight;

    public PreviewText(Context context, String text, Typeface tf, float X, float Y) {
        super(context);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(100);
        paint.setTypeface(tf);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        this.text = text;
        this.textWidth = paint.measureText(this.text);
        this.textHeight = paint.getTextSize() + paint.getTextSize() / 2;
        this.x = X;
        this.y = Y;
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.currentCanvas = canvas;
        canvas.drawText(text, x, paint.getTextSize() - paint.getTextSize() / 4, paint);
    }

    public int getTextWidth() {
        return (int) (this.textWidth + this.x);
    }

    public int getTextHeight() {
        return (int) textHeight;
    }

    @Override
    public void onClick(View view) {
        Paint recPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        recPaint.setAntiAlias(true);
        recPaint.setStyle(Paint.Style.STROKE);
        recPaint.setStrokeWidth(20);
        recPaint.setColor(Color.GREEN);
        Rect rec = new Rect(0, 0,this.getTextWidth(), this.getTextHeight());
        Log.d("Test", "test");
        this.currentCanvas.drawLine(150, 300, 500, 800, recPaint);
    }
}
