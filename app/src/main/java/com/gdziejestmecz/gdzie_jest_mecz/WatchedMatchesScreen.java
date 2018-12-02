package com.gdziejestmecz.gdzie_jest_mecz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gdziejestmecz.gdzie_jest_mecz.components.watchedMatchesScreen.WatchedMatchesListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncWatchedMatchesListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.RetrieveWatchedMatches;

import java.util.ArrayList;

public class WatchedMatchesScreen extends Activity implements AsyncWatchedMatchesListResponse {

    private ArrayList<Match> watchedMatchesList;
    private ListView watchedMatchesListContent;
    private RelativeLayout loadingMatches;
    private Button close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_matches_screen);

        initUI();
        getAndRenderMatches();
    }

    private void initUI() {
        this.watchedMatchesListContent = findViewById(R.id.watched_matches_list);
        this.close_btn = findViewById(R.id.watched_matches_close_btn);
        this.loadingMatches = findViewById(R.id.loading_matches);

        addEventListeners();
    }

    private void addEventListeners() {
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAndRenderMatches() {
        this.loadingMatches.setVisibility(View.VISIBLE);

        RetrieveWatchedMatches retrieveWatchedMatches = new RetrieveWatchedMatches(999);
        retrieveWatchedMatches.delegate = this;

        retrieveWatchedMatches.execute();
    }

    @Override
    public void retrieveWatchedMatchesProcessFinished(ArrayList<Match> matchList) {
        Log.e("[OK]", "Got watchedMatches!");
        this.watchedMatchesList = matchList;
        watchedMatchesListContent.setAdapter(new WatchedMatchesListAdapter(this, this.watchedMatchesList));

        this.loadingMatches.setVisibility(View.GONE);
    }
}