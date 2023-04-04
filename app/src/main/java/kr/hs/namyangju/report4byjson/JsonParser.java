package kr.hs.namyangju.report4byjson;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class JsonParser {
    private Context context;

    public JsonParser(Context context) {
        this.context = context;
    }

    public ArrayList<DTO> Parsing(String json) {
        ArrayList<DTO> dto = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject inroot = (JSONObject) root.getJSONObject("worldpopulation");
            JSONArray array = inroot.getJSONArray((String) inroot.names().get(0));
            for (int i = 0; i < array.length(); i++) {
                DTO _dto = new DTO();
                JSONObject object = (JSONObject) array.getJSONObject(i);
                String rank = object.getString((String) object.names().get(0));
                String country = object.getString((String) object.names().get(1));
                String population = object.getString((String) object.names().get(2));
                String flag = object.getString((String) object.names().get(3));
                _dto.setRank(rank);
                _dto.setCountry(country);
                _dto.setPopulation(population);
                _dto.setFlag(flag);
                dto.add(_dto);
            }
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", e.getMessage());
        }
        return dto;
    }
}