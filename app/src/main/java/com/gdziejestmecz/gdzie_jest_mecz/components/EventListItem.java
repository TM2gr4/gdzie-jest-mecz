package com.gdziejestmecz.gdzie_jest_mecz.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdziejestmecz.gdzie_jest_mecz.models.EventData;

public class EventListItem extends LinearLayout {

    public EventListItem(Context context, EventData eventData){
        super(context);

        styleComponent(context, eventData);
    }

    private void styleComponent(Context context, EventData eventData) {
        LinearLayout LL = new LinearLayout(context);
        LayoutParams params = new LayoutParams
                (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        params.setMargins(15, 5, 15, 5);
        LL.setLayoutParams(params);

        TextView teamOne = new TextView(context);
        teamOne.setText(eventData.teamOneName);
        LL.addView(teamOne);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
