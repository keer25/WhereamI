package com.example.root.whereami;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.root.whereami.PuzzleActivity.count;
//import static com.example.root.whereami.PuzzleActivity.countOccurence;
//import static com.example.root.whereami.PuzzleActivity.found;
import static com.example.root.whereami.PuzzleActivity.foundHash;

/**
 * Created by root on 11/4/17.
 */

public class CustomArrayAdapter extends ArrayAdapter<CharSequence> {

    private String[] items;


    public CustomArrayAdapter(Context context) {
        super(context, R.layout.support_simple_spinner_dropdown_item,
                context.getResources().getStringArray(R.array.items));
        items = context.getResources().getStringArray(R.array.items);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = (TextView) view;
        if (tv == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tv = (TextView) inflater.inflate(R.layout.support_simple_spinner_dropdown_item, null);
        }
        int ind;
        for (ind = 0 ; ind<12 ; ind++){
            if (foundHash.valueAt(ind).size() != count[ind]) break;
        }
        tv.setText(items[ind]);
        return tv;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
        if (foundHash.valueAt(position).size() == count[position]) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return tv;
    }

}
