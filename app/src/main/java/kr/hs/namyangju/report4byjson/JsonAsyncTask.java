package kr.hs.namyangju.report4byjson;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonAsyncTask extends AsyncTask<String, String, String> {
    Context context = null;

    public JsonAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream = null;
        InputStreamReader streamReader=null;
        BufferedReader reader = null;
        AssetManager manager = context.getAssets();
        String result = "";
        try {

            inputStream = manager.open("sawon.json");
            streamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(streamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                result += (line + '\n');
            }

        } catch (IOException e) {
            publishProgress(e.getMessage());
        } finally {
            try {
                reader.close();
                streamReader.close();
                inputStream.close();
            } catch (IOException e) {
                publishProgress(e.getMessage());
            }
        }
        return result;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
