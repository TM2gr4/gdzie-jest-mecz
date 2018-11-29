package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

import java.util.ArrayList;

public class MatchSpinnerListAdapter extends ArrayAdapter<Match> {
    private Context context;
    private ArrayList<Match> matchList;

    public MatchSpinnerListAdapter(Context context, ArrayList<Match> data) {
        super(context, -1, -1, data); //dubel id?
        this.context = context;
        this.matchList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Match match = super.getItem(position);
        TextView test = new TextView(context);
        test.setText(match.getHomeTeam().getName() + " - " + match.getAwayTeam().getName());

        return test;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);

        Match match = super.getItem(position);
        label.setText(match.getHomeTeam().getName() + " - " + match.getAwayTeam().getName());

        return label;
    }
}
