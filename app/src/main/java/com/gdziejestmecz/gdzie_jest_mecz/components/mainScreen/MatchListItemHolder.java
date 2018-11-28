package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.R;

public class MatchListItemHolder {
    private View row;
    private TextView dateText = null,
            timeText = null,
            homeTeamLabel = null,
            awayTeamLabel = null;
    private ImageView homeTeamLogo = null,
                        awayTeamLogo = null;

    public MatchListItemHolder(View row) {
        this.row = row;
    }

    public TextView getDateText() {
        if (this.dateText == null) {
            this.dateText = (TextView) row.findViewById(R.id.date);
        }
        return this.dateText;
    }

    public TextView getTimeText() {
        if (this.timeText == null) {
            this.timeText = (TextView) row.findViewById(R.id.time);
        }
        return this.timeText;
    }

    public TextView getHomeTeamLabel() {
        if (this.homeTeamLabel == null) {
            this.homeTeamLabel = (TextView) row.findViewById(R.id.home_team_label);
        }
        return this.homeTeamLabel;
    }

    public TextView getAwayTeamLabel() {
        if (this.awayTeamLabel == null) {
            this.awayTeamLabel = (TextView) row.findViewById(R.id.away_team_label);
        }
        return this.awayTeamLabel;
    }

    public ImageView getHomeTeamLogo() {
        if (this.homeTeamLogo == null) {
            this.homeTeamLogo = (ImageView) row.findViewById(R.id.home_team_logo);
        }
        return this.homeTeamLogo;
    }

    public ImageView getAwayTeamLogo() {
        if (this.awayTeamLogo == null) {
            this.awayTeamLogo = (ImageView) row.findViewById(R.id.away_team_logo);
        }
        return this.awayTeamLogo;
    }
}