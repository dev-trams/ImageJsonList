package kr.hs.namyangju.report4byjson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageAsyncTask extends AsyncTask<String, String, String> {
    Context context;

    private Bitmap bitmap;

    public ImageAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader streamReader=null;
        BufferedReader reader = null;
        String result = "";

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);


        } catch (IOException e) {
            publishProgress(e.getMessage());
        } finally {
            try {
                reader.close();
                streamReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                publishProgress(e.getMessage());
            }
        }
        return result;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

}
