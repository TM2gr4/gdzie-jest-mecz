package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.R;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

import java.util.ArrayList;


public class PubSpinnerListAdapter extends ArrayAdapter<Pub> {
    private Context context;
    private ArrayList<Pub> pubList;
    private LayoutInflater inflater;

    public PubSpinnerListAdapter(Context context, ArrayList<Pub> data) {
        super(context, -1, -1, data); //dubel id?
        this.context = context;
        this.pubList = data;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        Pub pub = super.getItem(position);

        RelativeLayout pubSpinnerRow = (RelativeLayout) inflater.inflate(R.layout.pubs_spinner_row, null, false);
        TextView pubName = (TextView) pubSpinnerRow.findViewById(R.id.pub_name_spinner_row);

        pubName.setText(pub.getName());
        return pubSpinnerRow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
