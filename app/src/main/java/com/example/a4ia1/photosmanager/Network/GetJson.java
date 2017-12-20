package com.example.a4ia1.photosmanager.Network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmois on 12/20/17.
 */

public class GetJson extends AsyncTask<String, Void, Void> {
    public static final String URL_FULL =
            "http://192.168.1.104:3000/images";

    public static final String URL_MIN =
            "http://192.168.1.104:3000/images/100x100";

    private GetJSONCustomListenerObject listener;

    public interface GetJSONCustomListenerObject {
        void onGetReady(List<ImageData> images);
    }

    public void setCustomObjectListener(GetJSONCustomListenerObject listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... urls) {
        HttpGet httpGet = new HttpGet(urls[0]);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String jsonString = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            jsonString = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonString == null || jsonString.isEmpty()) {
            listener.onGetReady(null);
            return null;
        }

        List<ImageData> imagesData = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray allImagesJson = jsonObj.getJSONArray("imagesList");
            for (int i = 0; i < allImagesJson.length(); i++) {
                JSONObject object = allImagesJson.getJSONObject(i);
                String imageName = object.getString("name");
                String imageSaveTime = object.getString("birthtime");

                imagesData.add(new ImageData(imageName, imageSaveTime));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onGetReady(imagesData);
        return null;
    }
}
