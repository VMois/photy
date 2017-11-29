package com.example.a4ia1.photosmanager.Activities;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.R;

import java.io.IOException;

public class LettersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);

        LinearLayout fontsLayout = (LinearLayout) findViewById(R.id.fonts_layout);
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
        }
    }
}
