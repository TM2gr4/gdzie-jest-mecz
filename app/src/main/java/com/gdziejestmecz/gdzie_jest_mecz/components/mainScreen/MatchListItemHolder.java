package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.R;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;

import org.w3c.dom.Text;

public class MatchListItemHolder {
    private View row;
    private TextView dateText = null,
            timeText = null,
            homeTeamLabel = null,
            awayTeamLabel = null,
            pubsCount = null;
    private ImageView homeTeamLogo = null,
                        awayTeamLogo = null;
    private CardView card = null;
    private ListView pubsList = null;

    private boolean isPubsListExpanded;

    public MatchListItemHolder(final Context context, View row, final Match match) {
        this.row = row;
        this.isPubsListExpanded = false;

        this.getPubsCount().setText(Integer.toString(match.getPubs().size()));

        this.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("{CLick]","Match Card");

                if (isPubsListExpanded) {
                    getPubsList().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0));
                } else {
                    getPubsList().setAdapter(new PubListAdapter(context, match.getPubs()));
                    int theSizeIWant = match.getPubs().size() * 40;
                    getPubsList().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, theSizeIWant));
                }
                isPubsListExpanded = !isPubsListExpanded;
            };
        });
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

    public TextView getPubsCount() {
        if (this.pubsCount == null) {
            this.pubsCount = (TextView) row.findViewById(R.id.pubs_count_counter);
        }
        return this.pubsCount;
    }

    public ListView getPubsList() {
        if (this.pubsList == null) {
            this.pubsList = (ListView) row.findViewById(R.id.pubs_list);
        }
        return this.pubsList;
    }

    public CardView getCardView() {
        if (this.card == null) {
            this.card = (CardView) row.findViewById(R.id.match_card);
        }
        return this.card;
    }
}