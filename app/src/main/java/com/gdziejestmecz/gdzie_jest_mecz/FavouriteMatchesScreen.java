package com.gdziejestmecz.gdzie_jest_mecz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gdziejestmecz.gdzie_jest_mecz.components.favouriteMatchesScreen.FavouriteMatchesListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncFavouriteMatchesListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.RetrieveFavouriteMatches;

import java.util.ArrayList;

public class FavouriteMatchesScreen extends Activity implements AsyncFavouriteMatchesListResponse {

    private ArrayList<Match> favouriteMatchesList;
    private ListView favouriteMatchesListContent;
    private RelativeLayout loadingMatches;
    private Button close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_matches_screen);

        initUI();
        getAndRenderMatches();
    }

    private void initUI() {
        this.favouriteMatchesListContent = findViewById(R.id.favourite_matches_list);
        this.close_btn = findViewById(R.id.favourite_matches_close_btn);
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

        RetrieveFavouriteMatches retrieveFavouriteMatches = new RetrieveFavouriteMatches();
        retrieveFavouriteMatches.delegate = this;

        retrieveFavouriteMatches.execute();
    }

    @Override
    public void retrieveFavouriteMatchesProcessFinished(ArrayList<Match> matchList) {
        Log.e("[OK]", "Got watchedMatches!");
        this.favouriteMatchesList = matchList;
        favouriteMatchesListContent.setAdapter(new FavouriteMatchesListAdapter(this, this.favouriteMatchesList));

        this.loadingMatches.setVisibility(View.GONE);
    }
}