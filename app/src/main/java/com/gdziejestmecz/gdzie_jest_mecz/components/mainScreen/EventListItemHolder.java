package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.view.View;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.R;

public class EventListItemHolder {
    private View row;
    private TextView dateText = null,
            timeText = null,
            teamsLabel = null;

    public EventListItemHolder(View row) {
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

    public TextView getTeamsLabel() {
        if (this.teamsLabel == null) {
            this.teamsLabel = (TextView) row.findViewById(R.id.teams_label);
        }
        return this.teamsLabel;
    }
}