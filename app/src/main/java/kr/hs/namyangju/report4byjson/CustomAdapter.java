package kr.hs.namyangju.report4byjson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DTO> {
    private Context context;
    private ArrayList<DTO> dto;

    public CustomAdapter(@NonNull Context context, ArrayList<DTO> dto) {
        super(context, R.layout.item, dto);
        this.context = context;
        this.dto = dto;
    }

    @Nullable
    @Override
    public DTO getItem(int position) {
        return dto.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        DTO dto = this.dto.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            holder.imageView = convertView.findViewById(R.id.image);
            holder.id = convertView.findViewById(R.id.id);
            holder.name = convertView.findViewById(R.id.name);
            holder.salary = convertView.findViewById(R.id.salary);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.id.setText("순위 : " + dto.getRank());
        holder.name.setText("국가 : " + dto.getCountry());
        holder.salary.setText("인구 : " + dto.getPopulation());

        ImageThread thread = new ImageThread(context, dto.getFlag());
        thread.start();
        try {
            thread.join();
            holder.imageView.setImageBitmap(thread.getBitmap());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView id;
        TextView name;
        TextView salary;
    }
}
