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

import java.util.ArrayList;

public class MatchSpinnerListAdapter extends ArrayAdapter<Match> {
    private Context context;
    private ArrayList<Match> matchList;

    private LayoutInflater inflater;

    public MatchSpinnerListAdapter(Context context, ArrayList<Match> data) {
        super(context, -1, -1, data);
        this.context = context;
        this.matchList = data;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        Match match = super.getItem(position);
        RelativeLayout matchSpinnerRow = (RelativeLayout) inflater.inflate(R.layout.matches_spinner_row, null, false);
        TextView matchName = (TextView) matchSpinnerRow.findViewById(R.id.match_name_spinner_row);

        matchName.setText(match.getHomeTeam().getName() + " - " + match.getAwayTeam().getName());
        return matchSpinnerRow;
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
