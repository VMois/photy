package com.example.a4ia1.photosmanager.Network;

/**
 * Created by vmois on 12/19/17.
 */
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpUpload extends AsyncTask<File, Integer, Void> {

    private Context context;

    private ProgressDialog pd;

    private static final String url = "http://vmois.eu-4.evennode.com/upload";

    public HttpUpload(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setMessage("Uploading Picture...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Void doInBackground(File...file) {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("file", new FileBody(file[0]));
        httpPost.setEntity(entity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpPost);
            String result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
            Log.d("File upload result", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        pd.dismiss();
    }
}
