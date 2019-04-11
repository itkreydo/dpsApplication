package com.example.shadr.navdrawer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    Bitmap mIcon11;

    public DownloadImageTask(){
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        try {
            URL url = new URL(urldisplay);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            mIcon11 = BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mIcon11;
    }
}
