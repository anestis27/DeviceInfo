package com.example.orion.leitourgika;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Orion on 14/1/2015.
 */
public class SpaceAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public SpaceAdapter(Context context, Cursor c){
        super(context,c);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view=null;
        view=inflater.inflate(R.layout.history_list_item,parent,false);

        TextView internalspace=(TextView) view.findViewById(R.id.internalspace);
        TextView externalspace=(TextView) view.findViewById(R.id.externalspace);

        internalspace.setText(cursor.getString(1)+"Gb");
        externalspace.setText(cursor.getString(2)+"Gb");

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView internalspace=(TextView) view.findViewById(R.id.internalspace);
        TextView externalspace=(TextView) view.findViewById(R.id.externalspace);

        internalspace.setText(cursor.getString(1)+"Gb");
        externalspace.setText(cursor.getString(2)+"Gb");

    }
}
