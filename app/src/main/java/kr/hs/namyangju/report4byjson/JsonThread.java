package kr.hs.namyangju.report4byjson;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonThread extends Thread{

    private Context context;
    private  String page;
    private Handler handler = new Handler();
    private StringBuilder builder = new StringBuilder();

    public JsonThread(Context context, String page) {
        this.context = context;
        this.page = page;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(page);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line +"\n");
            }
            reader.close();
            streamReader.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        super.run();
    }

    public String getResuit() {
        return  builder.toString();
    }

}
