package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.R;

public class PubListItemHolder {
    private View row;
    private TextView pubName = null;

    public PubListItemHolder(View row) {
        this.row = row;
    }

    public TextView getPubName() {
        if (this.pubName == null) {
            this.pubName = (TextView) row.findViewById(R.id.pub_name);
        }
        return this.pubName;
    }
}