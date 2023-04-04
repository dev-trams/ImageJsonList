package kr.hs.namyangju.report4byjson;

import android.content.Context;
import android.util.Log;
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
    private int state = 1;

    public CustomAdapter(@NonNull Context context, ArrayList<DTO> dto) {
        super(context, R.layout.item, dto);
        this.context = context;
        this.dto = dto;
//        this.state = state;
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
            convertView = inflater.inflate(R.layout.item, null);
            holder.imageView = convertView.findViewById(R.id.image);
            holder.id = convertView.findViewById(R.id.id);
            holder.name = convertView.findViewById(R.id.name);
            holder.salary = convertView.findViewById(R.id.salary);
        holder.id.setText("순위 : " + dto.getRank() != null ? dto.getRank() : "");
        holder.name.setText("국가 : " + dto.getCountry() != null ? dto.getCountry() : "");
        holder.salary.setText("인구 : " + dto.getPopulation() != null ? dto.getPopulation() : "");

//        if(state == 1) {
            ImageThread thread = new ImageThread(context, dto.getFlag());
            thread.start();
            try {
                thread.join();
                holder.imageView.setImageBitmap(thread.getBitmap());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//        } else if(state == 2) {
//            ImageAsyncTask asyncTask = new ImageAsyncTask(context);
//            holder.imageView.setImageBitmap(asyncTask.getBitmap());
//        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView id;
        TextView name;
        TextView salary;
    }
}
