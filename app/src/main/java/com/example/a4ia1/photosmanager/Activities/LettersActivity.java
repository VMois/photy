package com.example.a4ia1.photosmanager.Activities;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
    private EditText previewFontEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);
        getSupportActionBar().hide();
        previewFontEditText = (EditText) findViewById(R.id.preview_font_et);
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
            newTextView.setTextSize(45);
            newTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView clickedTextView = (TextView) view;
                    currentTypeFace = clickedTextView.getTypeface();
                    renderPreviewText(previewFontEditText.getText().toString());
                }
            });
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
               renderPreviewText(editable.toString());
            }
        };
        previewFontEditText.addTextChangedListener(textWatcher);
    }

    private void renderPreviewText(String newText) {
        previewLayout.removeAllViews();
        previewText = new PreviewText(
                getApplicationContext(),
                newText,
                currentTypeFace,
                previewLayout.getX(),
                previewLayout.getY() + (previewLayout.getHeight() / 2));
        previewLayout.addView(previewText);
    }
}
