package kr.hs.namyangju.report4byjson;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    private Context context;

    public JsonParser(Context context) {
        this.context = context;
    }

    public ArrayList<DTO> Parsing(String json) {
        ArrayList<DTO> noteBooks = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject temp = root.getJSONObject((String) root.names().get(0));
            JSONArray array = temp.getJSONArray((String) temp.names().get(0));
            for (int i = 0; i < array.length(); i++) {
                DTO dto = new DTO();
                JSONObject object = array.getJSONObject(i);
                String rank = object.getString((String) object.names().get(0));
                String country = object.getString((String) object.names().get(1));
                String population = object.getString((String) object.names().get(2));
                String flag = object.getString((String) object.names().get(3));
                dto.setRank(rank);
                dto.setCountry(country);
                dto.setPopulation(population);
                dto.setFlag(flag);
                noteBooks.add(dto);
            }
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return noteBooks;
    }
}