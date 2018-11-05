package com.gdziejestmecz.gdzie_jest_mecz.components;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.gdziejestmecz.gdzie_jest_mecz.R;
import com.gdziejestmecz.gdzie_jest_mecz.constants.Colors;
import com.gdziejestmecz.gdzie_jest_mecz.models.MatchData;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<MatchData> {
    private Context context;
    private ArrayList<MatchData> matchDataList;

    public EventListAdapter(Context context, ArrayList<MatchData> data) {
        super(context, -1, -1, data);
        this.context = context;
        this.matchDataList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(matchDataList.size() == 0){
            LinearLayout LLlistMainWrapper = new LinearLayout(context);
            LinearLayout.LayoutParams defaultLLparams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LLlistMainWrapper.setLayoutParams(defaultLLparams);

            TextView noEventsLabel = new TextView(context);
            noEventsLabel.setText("Ups! Brak meczyków :(");

            LLlistMainWrapper.addView(noEventsLabel);
            return LLlistMainWrapper;
        };

        return renderListItems(position);
    }

    private LinearLayout renderListItems(int position) {
        LinearLayout LLlistMainWrapper = new LinearLayout(context);
        LinearLayout.LayoutParams defaultLLparams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LLlistMainWrapper.setLayoutParams(defaultLLparams);

        MatchData matchData = super.getItem(position);

        SwipeLayout swipeLayout = new SwipeLayout(context);

        CardView eventCard = new CardView(context);
        eventCard.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));

        defaultLLparams.setMargins(15, 5, 15, 5);

        LinearLayout LLMainWrapper = new LinearLayout(context);
        LLMainWrapper.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout LLDateTimeWrapper = new LinearLayout(context);
        LinearLayout.LayoutParams paramsLLDateTimeWrapper = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LLDateTimeWrapper.setOrientation(LinearLayout.VERTICAL);
        paramsLLDateTimeWrapper.width = 200;
        paramsLLDateTimeWrapper.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramsLLDateTimeWrapper.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        LLDateTimeWrapper.setLayoutParams(paramsLLDateTimeWrapper);

        TextView nowLabel = new TextView(context);
        nowLabel.setText("Teraz");
        nowLabel.setWidth(80);
        nowLabel.setTextColor(Colors.boneWhite);
        nowLabel.setBackgroundColor(Colors.wineRed);
        nowLabel.setGravity(Gravity.CENTER);

        TextView date = new TextView(context);
        date.setText(matchData.date.split("-")[2] + "." + matchData.date.split("-")[1]);
        date.setTextColor(Colors.lightGray);
        date.setGravity(Gravity.CENTER);

        TextView time = new TextView(context);
        time.setText(matchData.time.split(":")[0] + ":" + matchData.time.split(":")[1]);
        time.setTextColor(Colors.blackyBlack);
        time.setTextSize(24);
        time.setGravity(Gravity.CENTER);

        Boolean eventHappeningNow = position == 0 ? true : false;
        if (eventHappeningNow) {
            LLDateTimeWrapper.addView(nowLabel);
        }

        LLDateTimeWrapper.addView(date);
        LLDateTimeWrapper.addView(time);

        LinearLayout LLTeamsAndLocation = new LinearLayout(context);
        LLTeamsAndLocation.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams paramsLLTeamsAndLocation = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsLLTeamsAndLocation.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        LLTeamsAndLocation.setLayoutParams(paramsLLTeamsAndLocation);

        LinearLayout LLLocationInfo = new LinearLayout(context);
        LLTeamsAndLocation.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramsLLLocationInfo = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsLLLocationInfo.gravity = Gravity.CENTER_HORIZONTAL;
        LLLocationInfo.setLayoutParams(paramsLLLocationInfo);

        ImageView markerIco = new ImageView(context);
        markerIco.setMaxWidth(8);
        markerIco.setMaxHeight(8);
        markerIco.setImageResource(R.drawable.ic_marker);

        int pubsCount = 3;
        TextView pubsCountLabel = new TextView(context);
        pubsCountLabel.setText("W miejscach: " + pubsCount);
        pubsCountLabel.setTextColor(Colors.lightGray);
        pubsCountLabel.setGravity(Gravity.CENTER);

        LLLocationInfo.addView(markerIco);
        LLLocationInfo.addView(pubsCountLabel);

        LinearLayout LLTeamsInfo = new LinearLayout(context);
        LinearLayout.LayoutParams paramsLLTeamsInfo = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsLLTeamsInfo.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        LLTeamsInfo.setLayoutParams(paramsLLTeamsInfo);

        ImageView teamOneLogo = new ImageView(context);
        LinearLayout.LayoutParams logoConstraints = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT);
        teamOneLogo.setLayoutParams(logoConstraints);

        teamOneLogo.setBackgroundResource(R.drawable.lfc_logo);

        ImageView teamTwoLogo = new ImageView(context);
        teamTwoLogo.setLayoutParams(logoConstraints);
        teamTwoLogo.setBackgroundResource(R.drawable.logo_real_madryt);

        TextView teams = new TextView(context);
        teams.setText(matchData.homeTeam.getName()+ " - " + matchData.awayTeam.getName());
        teams.setGravity(Gravity.CENTER);
        teams.setTextColor(Colors.blackyBlack);
        teams.setTextSize(15);

        LLTeamsInfo.addView(teamOneLogo);
        LLTeamsInfo.addView(teams);
        LLTeamsInfo.addView(teamTwoLogo);

        LLTeamsAndLocation.addView(LLLocationInfo);
        LLTeamsAndLocation.addView(LLTeamsInfo);

        LLMainWrapper.addView(LLDateTimeWrapper);
        LLMainWrapper.addView(LLTeamsAndLocation);

        eventCard.addView(LLMainWrapper);

        LLlistMainWrapper.addView(swipeLayout);

        LinearLayout LLSwipeBackground = new LinearLayout(context);
        LLSwipeBackground.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.MATCH_PARENT));
        LLSwipeBackground.setBackgroundColor(Colors.lightGray);
        LLSwipeBackground.setGravity(Gravity.CENTER);

        TextView swipeActionLabel = new TextView(context);
        swipeActionLabel.setTextColor(Color.WHITE);
        swipeActionLabel.setTextSize(18);

        ImageView icoBox = new ImageView(context);
        LLSwipeBackground.setOrientation(LinearLayout.HORIZONTAL);

        LLSwipeBackground.addView(icoBox);
        LLSwipeBackground.addView(swipeActionLabel);

        swipeLayout.addView(LLSwipeBackground);
        swipeLayout.addView(eventCard);

        handleSwipeAction(swipeLayout, LLSwipeBackground, swipeActionLabel, icoBox, position);
        return LLlistMainWrapper;
    }

    private void handleSwipeAction(SwipeLayout swipeLayout, final LinearLayout background, final TextView swipeActionLabel, final ImageView icoBox, final int position) {
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, background);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(final SwipeLayout layout, int leftOffset, int topOffset) {
                Log.d("SwipeEvent", "its being swipped");
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                if (layout.getDragEdge() == SwipeLayout.DragEdge.Left) {
                    Log.d("SwipeEvent", "dragged left");
                    background.setBackgroundColor(Colors.lapisBlue);
                    swipeActionLabel.setText("Dodano do obserwowanych");
                    icoBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border_white_48dp));

                } else {
                    Log.d("SwipeEvent", "dragged right");
                    background.setBackgroundColor(Color.RED);
                    swipeActionLabel.setText("Usunięty");
                    icoBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_white_48dp));
                }
            }

            @Override
            public void onOpen(final SwipeLayout layout) {
                Log.d("SwipeEvent", "onOpen");

                if (layout.getDragEdge() == SwipeLayout.DragEdge.Left) {
                    Log.d("SwipeEvent", "opened left");
                    //dodajDoObserwowanych()

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.close(true);
                        }
                    }, 1000);
                } else {
                    Log.d("SwipeEvent", "opened right");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deleteEvent(position);
                        }
                    }, 500);
                }
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                Log.d("SwipeEvent", "onHandRelease");
            }
        });
    }

    private void deleteEvent(int itemId){
        matchDataList.remove(itemId);
        Log.d("EventAction", "removed " + itemId);

        refreshEventList();
    }

    private void refreshEventList() {
        this.notifyDataSetChanged();
    }
}
