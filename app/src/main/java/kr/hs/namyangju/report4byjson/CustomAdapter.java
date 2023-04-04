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
    private ArrayList<DTO> noteBooks;

    public CustomAdapter(@NonNull Context context, ArrayList<DTO> noteBooks) {
        super(context, R.layout.item, noteBooks);
        this.context = context;
        this.noteBooks = noteBooks;
    }

    @Nullable
    @Override
    public DTO getItem(int position) {
        return noteBooks.get(position);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        DTO noteBook = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image);
            holder.brand = convertView.findViewById(R.id.brand);
            holder.model = convertView.findViewById(R.id.model);
            holder.price = convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.brand.setText(noteBook.getRank());
        holder.model.setText(noteBook.getCountry());
        holder.price.setText(noteBook.getPopulation());

        ImageThread thread = new ImageThread(context, noteBook.getFlag());
        thread.start();
        try {
            thread.join();
            holder.imageView.setImageBitmap(thread.getBitmap());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notifyDataSetChanged();
        return convertView;
    }
    public void updateData(ArrayList<DTO> items) {
        this.noteBooks = items;
        notifyDataSetChanged();
    }
    private class ViewHolder {
        ImageView imageView;
        TextView brand;
        TextView model;
        TextView price;
    }
}