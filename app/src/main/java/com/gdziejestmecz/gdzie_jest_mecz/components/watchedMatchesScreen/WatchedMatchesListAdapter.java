package com.gdziejestmecz.gdzie_jest_mecz.components.watchedMatchesScreen;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.gdziejestmecz.gdzie_jest_mecz.R;
import com.gdziejestmecz.gdzie_jest_mecz.constants.Colors;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.utils.DownloadImageTask;

import java.util.ArrayList;

public class WatchedMatchesListAdapter extends ArrayAdapter<Match> {
    private Context context;
    private ArrayList<Match> matchList;

    public WatchedMatchesListAdapter(Context context, ArrayList<Match> data) {
        super(context, -1, -1, data);
        this.context = context;
        this.matchList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WatchedMatchesListItemHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.watched_matches_list_row, null, false);
            holder = new WatchedMatchesListItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (WatchedMatchesListItemHolder) convertView.getTag();
        }

        Match match = matchList.get(position);
        String date = match.getDate().split("-")[2] + "." + match.getDate().split("-")[1];
        String time = match.getTime().split(":")[0] + ":" + match.getTime().split(":")[1];
        holder.getDateText().setText(date);
        holder.getTimeText().setText(time);
        holder.getHomeTeamLabel().setText(match.getHomeTeam().getName());
        holder.getAwayTeamLabel().setText(match.getAwayTeam().getName());
        holder.getPubsCount().setText(Integer.toString(match.getPubs().size()));

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.home_team_logo))
                .execute(match.getHomeTeam().getLogoURL());

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.away_team_logo))
                .execute(match.getAwayTeam().getLogoURL());

        return convertView;
    }


}