package com.example.orion.leitourgika;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Orion on 16/1/2015.
 */
public class ProcessAdapter extends ArrayAdapter<SimpleProcess> {
    public ProcessAdapter(Context context, ArrayList<SimpleProcess> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SimpleProcess proc = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.process_list_item, parent, false);
        }
        // Lookup view for data population
        TextView Name = (TextView) convertView.findViewById(R.id.processname);
        TextView Pid = (TextView) convertView.findViewById(R.id.pid);
        TextView Ram = (TextView) convertView.findViewById(R.id.ram);
        // Populate the data into the template view using the data object
        Name.setText(proc.getName());
        Pid.setText(proc.getPid()+"");
        Ram.setText(proc.getRam()+" MB");
        // Return the completed view to render on screen
        return convertView;
    }
}
