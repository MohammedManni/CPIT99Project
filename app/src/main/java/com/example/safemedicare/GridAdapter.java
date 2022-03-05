package com.example.safemedicare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter {

    ArrayList<GridItem> eventList;

    public GridAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        eventList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.gridview_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textViewItem);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageViewGrid);
        textView.setText(eventList.get(position).getEventListName());
        //imageView.setImageResource(eventList.get(position).getEventListImage());
        return v;

    }

}
