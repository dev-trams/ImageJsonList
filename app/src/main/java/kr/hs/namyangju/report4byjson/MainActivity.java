package kr.hs.namyangju.report4byjson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    DB db;
    Button btn;
    ListView viewLIST;
    int state = 1;
    ArrayList<Sawon> dtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DB(this, "db", null, 1);
        db.tables.setTable("sawon");
        SQLiteDatabase database = db.getWritableDatabase();
        btn = (Button) findViewById(R.id.btn_jsontosql);
        viewLIST = (ListView) findViewById(R.id.listview);
        viewLIST.setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonThread thread = new JsonThread(getBaseContext());
                JsonAsyncTask task = new JsonAsyncTask(getBaseContext());

                JsonParser parser = new JsonParser(MainActivity.this);
                //dtos = parser.onParsingJson(thread.getResuit()); //Thread용
                try {
                    dtos = parser.onParsingJson(task.execute().get()); //asyncTask용
                } catch (JSONException | ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(getBaseContext(), "완료했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        Button btn_desc = (Button) findViewById(R.id.btn_salary_desc);
        btn_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onList(2);
            }
        });
        Button btn_order = (Button) findViewById(R.id.btn_salary);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onList(3);
            }
        });
        Button btn_sex_m = (Button) findViewById(R.id.btn_male);
        btn_sex_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onList(4);
            }
        });
        Button btn_sex_f = (Button) findViewById(R.id.btn_female);
        btn_sex_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onList(5);
            }
        });
    }

    public void onList(int _state) {
        ArrayList<Sawon> sawons = new ArrayList<>();
        Cursor cursor = null;
        switch (_state) {
            case 2:
                cursor = db.onSearchDataDesc("sawon");
                break;
            case 3:
                cursor = db.onSearchDataOrder("sawon");
                break;
            case 4:
                cursor = db.onSearchDataGender("sawon", "남");
                break;
            case 5:
                cursor = db.onSearchDataGender("sawon", "여");
                break;
        }

        Log.d("TAG_CURSOR", String.valueOf(cursor.getCount()));
        String id = "";
        String name = "";
        String gender = "";
        String imgUrl = "";
        String salary = "";
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            id = cursor.getString(0);
            name = cursor.getString(1);
            gender = cursor.getString(2);
            imgUrl = cursor.getString(3);
            salary = cursor.getString(4);
            sawons.add(new Sawon(id, name, gender, imgUrl, salary));
        }

        cursor.close();
        CustomAdapter adapter = new CustomAdapter(MainActivity.this, sawons, state);
        viewLIST.setAdapter(adapter);
        viewLIST.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "아이템 클릭:" + i, Toast.LENGTH_SHORT).show();
                showPopup(sawons.get(i).getName(), Integer.parseInt(sawons.get(i).getId()), sawons.get(i).getImgUrl());

            }
        });
        Log.d("TAG", String.valueOf(viewLIST.getCount()));
    }

    private void showPopup(String name, int id, String imgUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = null;
        if (state == 1 || state == 2) {
            dialogView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        } else if (state == 3) {
            dialogView = LayoutInflater.from(this).inflate(R.layout.popup_layout_round, null);
        }

        ImageView imageView = dialogView.findViewById(R.id.imageView);
        TextView nameTextView = dialogView.findViewById(R.id.nameTextView);
        ImageView qrCodeImageView = dialogView.findViewById(R.id.qrCodeImageView);

        nameTextView.setText("선택한 사원(" + name + ")");

        Glide.with(this)
                .load(imgUrl)
                .centerCrop()
                .into(imageView);

        Bitmap qrCode = generateQRCode(name, id);
        qrCodeImageView.setImageBitmap(qrCode);

        builder.setView(dialogView);
        builder.setPositiveButton("확인", null); // OK 버튼 추가

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if (window != null) {
            // 다이얼로그 창의 속성 설정
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }


    private Bitmap generateQRCode(String name, int id) {
        String qrData = "Name: " + name + ", ID: " + id;

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 700, 700);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            return qrCodeBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.checked_select_back_music:
                // 배경 음악 체크 박스 클릭 시 동작
                if (item.isChecked()) {
                    // 체크되어 있으면 배경 음악 재생
                    stopBackgroundMusic();
                    item.setChecked(false);
                } else {
                    // 체크 해제되어 있으면 배경 음악 정지
                    playBackgroundMusic();
                    item.setChecked(true);
                }
//                item.setChecked(false);
                break;
            case R.id.rectangle_image:
                // 사각형 이미지
                state = 2;
                Toast.makeText(this, "lo:" + state, Toast.LENGTH_SHORT).show();
                item.setChecked(true);

                break;
            case R.id.round_shape_image:
                // 원형 이미지
                state = 3;
                Toast.makeText(this, "lo:" + state, Toast.LENGTH_SHORT).show();
                item.setChecked(true);

                break;
        }

        return true;
    }
    private MediaPlayer mediaPlayer;

    private void playBackgroundMusic() {
        if (mediaPlayer == null) {
            try {
                // MediaPlayer 객체 생성
                mediaPlayer = new MediaPlayer();

                // 음악 파일 로드
                AssetFileDescriptor descriptor = getAssets().openFd("background_music.mp3");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                // 필요한 설정
                mediaPlayer.setLooping(true); // 음악을 반복 재생할지 여부 설정
                mediaPlayer.prepare(); // 비동기적으로 음악을 준비
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 음악 재생
        mediaPlayer.start();
    }

    private void stopBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // 음악을 정지하고 MediaPlayer 객체 해제
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}