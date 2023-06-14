package kr.hs.namyangju.report4byjson;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    private Context context;

    public JsonParser(Context context) {
        this.context = context;
    }
    @NonNull
    public ArrayList<Sawon> onParsingJson(String json) throws JSONException {
        DB db = new DB(context, "db", null, 1);
        SQLiteDatabase database = db.getWritableDatabase();
        db.onCreate(database);
        ArrayList<Sawon> sawonList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray employeeArray = jsonObject.getJSONArray("Employee");

            for (int i = 0; i < employeeArray.length(); i++) {
                JSONObject employeeObject = employeeArray.getJSONObject(i);

                String id = employeeObject.getString("id");
                String name = employeeObject.getString("name");
                String gender = employeeObject.getString("gender");
                int salary = employeeObject.getInt("salary");
                String imageUrl = employeeObject.getString("image");

                db.insertData("sawon", id, name, gender, salary, imageUrl);
                Sawon sawon = new Sawon();
                sawon.setId(id);
                sawon.setName(name);
                sawon.setGender(gender);
                sawon.setSalary(String.valueOf(salary));
                sawon.setImgUrl(imageUrl);

                sawonList.add(sawon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sawonList;
    }
}
