package com.example.safemedicare;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapterMedicationLog extends ArrayAdapter {

    ArrayList<Medication> medicationArrayList;

    public GridAdapterMedicationLog(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        medicationArrayList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_adapter_medication_log, null);
        TextView medName = (TextView) v.findViewById(R.id.View4);
        TextView dateStarted = (TextView) v.findViewById(R.id.View5);
        TextView numberPerDay = (TextView) v.findViewById(R.id.View6);
        medName.setText(medicationArrayList.get(position).getMedicineName());
        dateStarted.setText(medicationArrayList.get(position).getStartDayDate());
        numberPerDay.setText(medicationArrayList.get(position).getNumberOfTime());
        return v;

    }

}
