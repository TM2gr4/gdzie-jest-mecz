package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchListAdapter extends ArrayAdapter<Match> {
    private Context context;
    private ArrayList<Match> matchList;
    private boolean isPubsListExpanded;

    public MatchListAdapter(Context context, ArrayList<Match> data) {
        super(context, -1, -1, data);
        this.context = context;
        this.matchList = data;

        this.isPubsListExpanded = false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MatchListItemHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final Match match = matchList.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.match_list_row, null, false);
            holder = new MatchListItemHolder(context, convertView, match);
            convertView.setTag(holder);

        } else {
            holder = (MatchListItemHolder) convertView.getTag();
        }

        String date = match.getDate().split("-")[2] + "." + match.getDate().split("-")[1];
        String time = match.getTime().split(":")[0] + ":" + match.getTime().split(":")[1];
        holder.getDateText().setText(date);
        holder.getTimeText().setText(time);
        holder.getHomeTeamLabel().setText(match.getHomeTeam().getName());
        holder.getAwayTeamLabel().setText(match.getAwayTeam().getName());

        Picasso.get().load(match.getHomeTeam().getLogoURL()).into((ImageView) convertView.findViewById(R.id.home_team_logo));
        Picasso.get().load(match.getAwayTeam().getLogoURL()).into((ImageView) convertView.findViewById(R.id.away_team_logo));

        LinearLayout swipeBackground = (LinearLayout) convertView.findViewById(R.id.swipe_background);
        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_layout);
        TextView swipeActionLabel = (TextView) convertView.findViewById(R.id.swipe_action_label);
        ImageView icoBox = (ImageView) convertView.findViewById(R.id.ico_box);
        handleSwipeAction(position, swipeLayout, swipeBackground, swipeActionLabel, icoBox);

        return convertView;
    }
    private void pubsListToggle(final MatchListItemHolder holder, Match match, int position) {

    }
    private void handleSwipeAction(final int position, final SwipeLayout swipeLayout, final LinearLayout swipeBackground, final TextView swipeActionLabel, final ImageView icoBox) {
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeBackground);

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
                    swipeBackground.setBackgroundColor(Colors.lapisBlue);
                    swipeActionLabel.setText("Dodano do obserwowanych");
                    icoBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border_white_48dp));

                } else {
                    Log.d("SwipeEvent", "dragged right");
                    swipeBackground.setBackgroundColor(Color.RED);
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
                    }, 0);
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
        try {
            matchList.remove(itemId);
            Log.d("EventAction", "removed " + itemId);
        } catch(Exception e) {
            Toast.makeText(context, "Blad! Spróbuj później", Toast.LENGTH_SHORT).show();
        }
        refreshEventList();
    }

    private void refreshEventList() {
        this.notifyDataSetChanged();
    }
}