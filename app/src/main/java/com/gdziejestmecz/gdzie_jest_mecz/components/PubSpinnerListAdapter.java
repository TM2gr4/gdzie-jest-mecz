package com.gdziejestmecz.gdzie_jest_mecz.components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

import java.util.ArrayList;

public class PubSpinnerListAdapter extends ArrayAdapter<Pub> {
    private Context context;
    private ArrayList<Pub> pubList;

    public PubSpinnerListAdapter(Context context, ArrayList<Pub> data) {
        super(context, -1, -1, data); //dubel id?
        this.context = context;
        this.pubList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Pub pub = super.getItem(position);
        TextView test = new TextView(context);
        test.setText(pub.getName());

        return test;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        Pub pub = super.getItem(position);
        label.setText(pub.getName());

        return label;
    }
}
