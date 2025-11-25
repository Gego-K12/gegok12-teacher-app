package com.gegosoft.yourappname.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gegosoft.yourappname.R;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<String> objects;
    public SpinnerAdapter(ArrayList<String> objects, Context context1) {
        this.objects =objects;
        this.context = context1;
        inflter = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return objects.size();
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
        if (view==null)
            view = inflter.inflate(R.layout.spinner_value_layout, null);
        TextView spinnertext = (TextView) view.findViewById(R.id.spinnertext);
        spinnertext.setText(objects.get(i));
        return view;
    }
}