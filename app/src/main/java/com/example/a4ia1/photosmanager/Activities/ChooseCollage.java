package com.example.a4ia1.photosmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.ImageData;
import com.example.a4ia1.photosmanager.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseCollage extends AppCompatActivity {
    private List<ImageData> collageImagesList;
    private int[][][] collages;
    private View.OnClickListener collageChooseClickFunction;
    private ImageButton collageButtonSimple1;
    private ImageButton collageButtonSimple2;
    private ImageButton collageButtonComplicated1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_collage);
        collages = new int[][][] {
                // first collage - simple
                {
                        {0, 0, 50, 100},
                        {50, 0, 50, 100}
                },
                // second collage - simple
                {
                        {0, 0, 100, 70},
                        {0, 70, 50, 30},
                        {50, 70, 50, 30}
                },
                // third collage - complicated
                {
                        {0, 0, 20, 30},
                        {20, 0, 40, 30},
                        {60, 0, 40, 60},
                        {0, 30, 60, 30},
                        {0, 60, 40, 40},
                        {40, 60, 60, 40}
                }
        };
        collageImagesList = new ArrayList<>();
        collageChooseClickFunction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.collage_choose_simple_1:
                        onCollageClick(0);
                        break;
                    case R.id.collage_choose_simple_2:
                        onCollageClick(1);
                        break;
                    case R.id.collage_choose_complicated_1:
                        onCollageClick(2);
                        break;
                }
            }
        };
        collageButtonSimple1 = (ImageButton) findViewById(R.id.collage_choose_simple_1);
        collageButtonSimple2 = (ImageButton) findViewById(R.id.collage_choose_simple_2);
        collageButtonComplicated1 = (ImageButton) findViewById(R.id.collage_choose_complicated_1);

        collageButtonSimple1.setOnClickListener(collageChooseClickFunction);
        collageButtonSimple2.setOnClickListener(collageChooseClickFunction);
        collageButtonComplicated1.setOnClickListener(collageChooseClickFunction);
    }

    public void onCollageClick(int collageId) {
        collageImagesList = new ArrayList<>();
        for(int i = 0; i < collages[collageId].length; i++) {
            int[] opt = collages[collageId][i];
            collageImagesList.add(new ImageData(opt[0], opt[1], opt[2], opt[3]));
        }
        Intent intent = new Intent(getApplicationContext(), CollageActivity.class);
        intent.putExtra("collage", (Serializable) collageImagesList);
        startActivity(intent);
    }
}
