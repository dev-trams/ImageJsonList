package kr.hs.namyangju.report4byjson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView viewJSON;
    Button btn1;
    Button btn2;
    ListView viewLIST;
    String url = "http://10.50.0.61:8887/";
    String page = url + "population.json";
    int state = 1;
    String json = "";
    ArrayList<DTO> dtos;
    ScrollView scrollViewJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        viewJSON = (TextView) findViewById(R.id.viewJSON);
        viewLIST = (ListView) findViewById(R.id.viewLIST);
        scrollViewJson = (ScrollView) findViewById(R.id.scrollViewJSON);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewJson.setVisibility(View.VISIBLE);
                viewJSON.setVisibility(View.VISIBLE);
                viewLIST.setVisibility(View.GONE);
                if(state == 1) {

                    JsonThread thread = new JsonThread(MainActivity.this, page);
                    thread.start();
                    try {
                        thread.join();
                        json = thread.getResuit();
                        viewJSON.setText(json);
                    } catch (InterruptedException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if(state == 2){
                    JsonAsyncTask jsonAsyncTask = new JsonAsyncTask();
                    try {
                        json = jsonAsyncTask.execute(page).get();
                        viewJSON.setText(json);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewJson.setVisibility(View.GONE);
                viewJSON.setVisibility(View.GONE);
                viewLIST.setVisibility(View.VISIBLE);
                if (state == 1) {
                    JsonThread thread = new JsonThread(MainActivity.this, page);
                    thread.start();
                    try {
                        thread.join();
                        JsonParser parser = new JsonParser(MainActivity.this);
                        dtos = parser.Parsing(thread.getResuit());
                        Log.d("TAG", "dto "+dtos.get(0).getFlag());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    CustomAdapter adapter = new CustomAdapter(MainActivity.this, dtos);
                    viewLIST.setAdapter(adapter);
                    Log.d("TAG", String.valueOf(viewLIST.getCount()));
                } else if(state == 2){
                    JsonAsyncTask jsonAsyncTask = new JsonAsyncTask();
                    try {
                        json = jsonAsyncTask.execute(page).get();
                        JsonParser parser = new JsonParser(MainActivity.this);
                        dtos = parser.Parsing(json);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    CustomAdapter adapter = new CustomAdapter(MainActivity.this, dtos);
                    viewLIST.setAdapter(adapter);

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemThread:
                state = 1;
                break;
            case R.id.itemAsync:
                state = 2;
                break;
        }
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}