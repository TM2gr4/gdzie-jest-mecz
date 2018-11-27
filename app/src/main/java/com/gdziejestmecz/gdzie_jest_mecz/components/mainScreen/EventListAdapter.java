package com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.gdziejestmecz.gdzie_jest_mecz.R;
import com.gdziejestmecz.gdzie_jest_mecz.constants.Colors;
import com.gdziejestmecz.gdzie_jest_mecz.models.Event;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> eventList;

    public EventListAdapter(Context context, ArrayList<Event> data) {
        super(context, R.layout.event_list_row, R.id.event_list_row, data);
        this.context = context;
        this.eventList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventListItemHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_list_row, null, false);
            holder = new EventListItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (EventListItemHolder) convertView.getTag();
        }

        Event event = eventList.get(position);
        String date = event.getMatch().getDate().split("-")[1] + ":" + event.getMatch().getDate().split("-")[2];
        String time = event.getMatch().getTime();
        holder.getDateText().setText(date);
        holder.getTimeText().setText(time);
        holder.getTeamsLabel().setText(event.getMatch().getHomeTeam().getName() + " - " + event.getMatch().getAwayTeam().getName());
//        LinearLayout swipeBackground = (LinearLayout) convertView.findViewById(R.id.swipe_background);
//        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_layout);
//        TextView swipeActionLabel = (TextView) convertView.findViewById(R.id.swipe_action_label);
//        handleSwipeAction(position, swipeLayout, swipeBackground, swipeActionLabel);
        return convertView;
    }

    private void handleSwipeAction(final int position, final SwipeLayout swipeLayout, final LinearLayout swipeBackground, final TextView swipeActionLabel) {
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
//                    icoBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border_white_48dp));

                } else {
                    Log.d("SwipeEvent", "dragged right");
                    swipeBackground.setBackgroundColor(Color.RED);
                    swipeActionLabel.setText("Usunięty");
//                    icoBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_white_48dp));
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
        eventList.remove(itemId);
        Log.d("EventAction", "removed " + itemId);

        refreshEventList();
    }

    private void refreshEventList() {
        this.notifyDataSetChanged();
    }
}