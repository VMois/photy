package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.a4ia1.photosmanager.R;

/**
 * Created by vmois on 12/13/17.
 */

public class ColorPicker extends RelativeLayout implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context context;
    private ImageView popupImage;
    private ColorPickerCustomListenerObject listener;

    public interface ColorPickerCustomListenerObject {
        void onColorPick(int pickedColor);
    }

    public ColorPicker(Context context) {
        super(context);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        setOnClickListener(this);
        this.listener = null;
        init();
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        setOnClickListener(this);
        this.listener = null;
        init();
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        setOnClickListener(this);
        this.listener = null;
        init();
    }

    public void setCustomObjectListener(ColorPickerCustomListenerObject listener) {
        this.listener = listener;
    }

    private void init() {
        mInflater.inflate(R.layout.color_picker, this, true);
    }

    @Override
    public void onClick(View view) {
        // get root view of Activity
        View rootView = view.getRootView();

        // get popup layout
        View popupLayout = mInflater.inflate(R.layout.color_picker_popup,
                (ViewGroup) findViewById(R.id.popup_element));
        popupImage = popupLayout.findViewById(R.id.color_picker_popup_image);
        ImageView exitImageView = popupLayout.findViewById(R.id.popup_exit);
        popupImage.setDrawingCacheEnabled(true);
        popupImage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        Bitmap bmp = popupImage.getDrawingCache();
                        int color = bmp.getPixel((int)motionEvent.getX(), (int)motionEvent.getY());
                        listener.onColorPick(color);
                        break;
                }
                return true;
            }
        });

        // create popup and show
        final PopupWindow pw = new PopupWindow(popupLayout, rootView.getWidth(), rootView.getHeight(), true);
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        exitImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });
    }
}
