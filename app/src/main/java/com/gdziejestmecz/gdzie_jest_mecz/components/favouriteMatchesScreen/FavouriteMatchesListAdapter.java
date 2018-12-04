package com.gdziejestmecz.gdzie_jest_mecz.components.favouriteMatchesScreen;

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
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncDeleteFavouritedMatchesListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.DeleteFavouritedMatch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteMatchesListAdapter extends ArrayAdapter<Match> implements AsyncDeleteFavouritedMatchesListResponse {
    private Context context;
    private ArrayList<Match> matchList;

    public FavouriteMatchesListAdapter(Context context, ArrayList<Match> data) {
        super(context, -1, -1, data);
        this.context = context;
        this.matchList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FavouriteMatchesListItemHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.favourite_matches_list_row, null, false);
            holder = new FavouriteMatchesListItemHolder(convertView);

            LinearLayout swipeBackground = (LinearLayout) convertView.findViewById(R.id.swipe_background);
            SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_layout);
            TextView swipeActionLabel = (TextView) convertView.findViewById(R.id.swipe_action_label);
            ImageView icoBox = (ImageView) convertView.findViewById(R.id.ico_box);
            handleSwipeAction(position, swipeLayout, swipeBackground, swipeActionLabel, icoBox);

            convertView.setTag(holder);
        } else {
            holder = (FavouriteMatchesListItemHolder) convertView.getTag();
        }

        Match match = matchList.get(position);
        String date = match.getDate().split("-")[2] + "." + match.getDate().split("-")[1];
        String time = match.getTime().split(":")[0] + ":" + match.getTime().split(":")[1];
        holder.getDateText().setText(date);
        holder.getTimeText().setText(time);
        holder.getHomeTeamLabel().setText(match.getHomeTeam().getName());
        holder.getAwayTeamLabel().setText(match.getAwayTeam().getName());
        holder.getPubsCount().setText(Integer.toString(match.getPubs().size()));

        Picasso.get().load(match.getHomeTeam().getLogoURL()).into((ImageView) convertView.findViewById(R.id.home_team_logo));
        Picasso.get().load(match.getAwayTeam().getLogoURL()).into((ImageView) convertView.findViewById(R.id.away_team_logo));

        return convertView;
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
                if(layout.getDragEdge() == SwipeLayout.DragEdge.Left) {

                } else {
                    Log.d("SwipeEvent", "dragged right");
                    swipeBackground.setBackgroundColor(Color.RED);
                    swipeActionLabel.setText("Nie obserwujesz");
                    icoBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_white_48dp));
                }
            }

            @Override
            public void onOpen(final SwipeLayout layout) {
                Log.d("SwipeEvent", "onOpen");

                if (layout.getDragEdge() == SwipeLayout.DragEdge.Right) {
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
            disfavourMatch(matchList.get(itemId));
            Log.e("EventAction", "removed favorite event" + itemId);
        } catch(Exception e) {
            Toast.makeText(context, "Blad! Spróbuj później", Toast.LENGTH_SHORT).show();
        }

        refreshMatchesList();
    }

    private void disfavourMatch(Match match) {
        DeleteFavouritedMatch deleteFavouritedMatch = new DeleteFavouritedMatch(match);
        deleteFavouritedMatch.delegate = this;
        deleteFavouritedMatch.execute();
    }

    private void refreshMatchesList() {
        this.notifyDataSetChanged();
    }

    @Override
    public void retrieveDeleteFavouritedMatchesProcessFinished(Match match) {
        if (match != null) {
            matchList.remove(match);
            refreshMatchesList();
        } else {
            Toast.makeText(context, "Blad! Spróbuj później", Toast.LENGTH_SHORT).show();
        }
    }
}