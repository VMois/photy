package com.example.a4ia1.photosmanager.Activities;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.PreviewText;
import com.example.a4ia1.photosmanager.R;

import java.io.IOException;

public class LettersActivity extends AppCompatActivity {
    private RelativeLayout previewLayout;
    private PreviewText previewText;
    private Typeface currentTypeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);
        getSupportActionBar().hide();
        EditText previewFontEditText = (EditText) findViewById(R.id.preview_font_et);
        LinearLayout fontsLayout = (LinearLayout) findViewById(R.id.fonts_layout);
        previewLayout = (RelativeLayout) findViewById(R.id.preview_layout);
        previewLayout.bringToFront();
        AssetManager assetManager = getAssets();
        String[] fontsList = {};
        try {
            fontsList = assetManager.list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String font : fontsList) {
            TextView newTextView = new TextView(getApplicationContext());
            Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/" + font);
            newTextView.setTypeface(tf);
            newTextView.setText(Constants.FONTS_DEFAULT_PREVIEW_TEXT);
            newTextView.setTextSize(35);
            fontsLayout.addView(newTextView);
            currentTypeFace = tf;
        }

        TextWatcher textWatcher = new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                previewLayout.removeAllViews();
                previewText = new PreviewText(
                        getApplicationContext(),
                        editable.toString(),
                        currentTypeFace,
                        previewLayout.getX(),
                        125);
                previewLayout.addView(previewText);
            }
        };
        previewFontEditText.addTextChangedListener(textWatcher);
    }
}
