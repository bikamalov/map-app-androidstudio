package com.example.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    private Context mContext;
    private List<Places> list;
    private DatabaseHelper myDB;


    public CustomAdapter(@NonNull Context context, List<Places>list) {
        super(context,R.layout.list_item);
        this.list = list;
        myDB = new DatabaseHelper(getContext());
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item,parent,false);

        final TextView tvName = customView.findViewById(R.id.location_name);
       // final TextView tvDescription = customView.findViewById(R.id.location_description);
        tvName.setText(list.get(position).getTitle());
        //tvDescription.setText(list.get(position).getDescription());
        return customView;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    public void setContents(List<Places> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
