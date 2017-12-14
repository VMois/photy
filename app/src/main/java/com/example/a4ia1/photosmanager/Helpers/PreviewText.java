package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by vmois on 11/29/17.
 */

public class PreviewText extends View implements View.OnClickListener, View.OnTouchListener {
    private Paint paint;
    private String text;
    private float x;
    private float y;
    private int baseColor;
    private int strokeColor;
    private float textWidth;
    private float textHeight;
    private int drawCommand;

    public PreviewText(Context context, String text, int baseColor, int strokeColor, Typeface tf, float X, float Y) {
        super(context);
        this.x = X;
        this.y = Y;
        this.text = text;
        this.drawCommand = -1;
        this.baseColor = baseColor;
        this.strokeColor = strokeColor;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(100);
        paint.setTypeface(tf);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(baseColor);

        Rect rect = new Rect();
        if (!this.text.isEmpty()) {
            paint.getTextBounds(this.text, 0, this.text.length(), rect);
        }
        this.textWidth = rect.width();
        this.textHeight = rect.height() + 50;
        setOnTouchListener(this);
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(baseColor);
        canvas.drawText(text, 0, this.getTextHeight() - 50, paint);
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawText(text, 0, this.getTextHeight() - 50, paint);

        switch (this.drawCommand) {
            // set focus draw rectangle
            case 0:
                Paint recPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                recPaint.setAntiAlias(true);
                recPaint.setStyle(Paint.Style.STROKE);
                recPaint.setStrokeWidth(5);
                recPaint.setColor(Color.GREEN);
                canvas.drawRect(0, 0, this.getTextWidth(), getTextHeight(), recPaint);
                break;
            case 1:
                break;
        }
    }

    public int getTextWidth() {
        return (int) this.textWidth;
    }

    public int getTextHeight() {
        return (int) textHeight;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float newX = motionEvent.getRawX() - getTextWidth() / 2;
        float newY = motionEvent.getRawY() - getTextHeight() / 2;
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.drawCommand = 0;
                this.bringToFront();
                this.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                view.setX(newX);
                view.setY(newY);
                break;
            case MotionEvent.ACTION_UP:
                view.setX(newX);
                view.setY(newY);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

    }
}
