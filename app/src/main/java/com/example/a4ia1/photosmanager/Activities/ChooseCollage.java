package com.example.a4ia1.photosmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.ImageData;
import com.example.a4ia1.photosmanager.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseCollage extends AppCompatActivity {
    private List<ImageData> collageImagesList;
    private Button testButton;
    private int[][][] collages;

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
                        {0, 0, 100, 80},
                        {0, 80, 50, 20},
                        {50, 80, 50, 20}
                },
                // third collage - complicated
                {
                        {0, 0, 100, 80},
                        {0, 80, 50, 20},
                        {50, 80, 50, 20},
                        {0, 0, 100, 80},
                        {0, 80, 50, 20},
                        {50, 80, 50, 20}
                }
        };
        collageImagesList = new ArrayList<>();
        testButton = (Button) findViewById(R.id.c1);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCollageClick(0);
            }
        });
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
