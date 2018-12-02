package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gdziejestmecz.gdzie_jest_mecz.R;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;

import java.util.ArrayList;

public class PubListAdapter extends ArrayAdapter<Pub> {
    private Context context;
    private ArrayList<Pub> pubsList;

    public PubListAdapter(Context context, ArrayList<Pub> data) {
        super(context, -1, -1, data);
        this.context = context;
        this.pubsList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PubListItemHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pub_list_row, null, false);
            holder = new PubListItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PubListItemHolder) convertView.getTag();
        }

        Pub pub = pubsList.get(position);
        holder.getPubName().setText(pub.getName());

        return convertView;
    }
}