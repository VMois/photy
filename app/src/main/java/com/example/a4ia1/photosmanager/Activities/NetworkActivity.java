package com.example.a4ia1.photosmanager.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.Network.GetJson;
import com.example.a4ia1.photosmanager.Network.ImageData;
import com.example.a4ia1.photosmanager.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NetworkActivity extends AppCompatActivity {
    private int smallImagesCount;
    private int startSmallImageCount;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        GetJson getMinImagesJson = new GetJson();
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        getMinImagesJson.setCustomObjectListener(new GetJson.GetJSONCustomListenerObject() {
            @Override
            public void onGetReady(List<ImageData> response) {
                if (response != null) {
                    smallImagesCount = response.size();
                    startSmallImageCount = 0;
                    for(ImageData data: response) {
                        new LoadImageTask().execute(data.getImageName(), data.getImageSaveTime(), "" + data.getImageSize());
                    }

                }
            }
        });
        getMinImagesJson.execute(GetJson.URL_MIN);
    }

    public class LoadImageTask extends AsyncTask<String, Void, Void> {
        public static final String URL_MIN =
                "http://vmois.eu-4.evennode.com/min/";
        public static final String URL_FULL =
                "http://vmois.eu-4.evennode.com/images/";

        private Drawable loadedImage;
        private String saveTime;
        private String name;
        private double size;

        public Drawable LoadImageFromWeb(String url) {
            try {
                InputStream inputStream = (InputStream) new URL(url).getContent();
                return Drawable.createFromStream(inputStream, "src name");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected Void doInBackground(String... urls) {
            loadedImage = LoadImageFromWeb(LoadImageTask.URL_MIN + urls[0]);
            saveTime = urls[1];
            name = urls[0];
            // in MB
            size = Integer.parseInt(urls[2]) / 10000.0;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("TEST", "ON POAST LOAD");
            startSmallImageCount++;
            View temp = LayoutInflater.from(getApplicationContext()).inflate(R.layout.network_image, null);
            ImageView iv = temp.findViewById(R.id.network_image);
            TextView tv = temp.findViewById(R.id.network_text);
            iv.setImageDrawable(loadedImage);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(LoadImageTask.URL_FULL + name));
                    startActivity(intent);
                }
            });
            tv.setText(saveTime + ", " + size + " MB");
            linearLayout.addView(temp);
        }
    }

}
