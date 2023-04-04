package kr.hs.namyangju.report4byjson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        viewJSON = (TextView) findViewById(R.id.viewJSON);
        viewLIST = (ListView) findViewById(R.id.viewLIST);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewJSON.setVisibility(View.VISIBLE);
                viewLIST.setVisibility(View.INVISIBLE);
                JsonThread thread = new JsonThread(MainActivity.this, page);
                thread.start();
                try {
                    thread.join();
                    json = thread.getResuit();
                    viewJSON.setText(json);
                } catch (InterruptedException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewJSON.setVisibility(View.INVISIBLE);
                viewLIST.setVisibility(View.VISIBLE);
                if (state == 1) {
                    JsonThread thread = new JsonThread(MainActivity.this, page);
                    thread.start();
                    try {
                        thread.join();
                        JsonParser jsonParser = new JsonParser(MainActivity.this);
                        dtos = jsonParser.Parsing(thread.getResuit());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, dtos);
                    viewLIST.setAdapter(customAdapter);
                    customAdapter.updateData(dtos);
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