package com.example.safemedicare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdabter extends BaseAdapter {
    Context context;
    String arrayList;
    //int flags[];
    LayoutInflater inflter;
    int x;

    public ListAdabter(Context context, String arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return x;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.listview_item, null);
        TextView patientName = (TextView) view.findViewById(R.id.patientINlistView);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        patientName.setText(arrayList);
        //icon.setImageResource(flags[i]);
        return view;
    }
}