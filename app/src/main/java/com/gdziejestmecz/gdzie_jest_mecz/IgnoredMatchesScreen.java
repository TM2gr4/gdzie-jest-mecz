package com.gdziejestmecz.gdzie_jest_mecz;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gdziejestmecz.gdzie_jest_mecz.components.ignoredMatchesScreen.IgnoredMatchesListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncIgnoredMatchesListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.RetrieveIgnoredMatches;

import java.util.ArrayList;

public class IgnoredMatchesScreen extends Activity implements AsyncIgnoredMatchesListResponse {

    private ArrayList<Match> ignoredMatchesList;
    private ListView ignoredListContent;
    private RelativeLayout loadingMatches;
    private Button close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignored_matches_screen);

        initUI();
        getAndRenderMatches();
    }

    private void initUI() {
        this.ignoredListContent = findViewById(R.id.ignored_matches_list);
        this.close_btn = findViewById(R.id.ignored_matches_close_btn);
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

        RetrieveIgnoredMatches retrieveIgnoredMatches = new RetrieveIgnoredMatches();
        retrieveIgnoredMatches.delegate = this;
        retrieveIgnoredMatches.execute();
    }

    @Override
    public void retrieveIgnoredMatchesProcessFinished(ArrayList<Match> matchList) {
        Log.e("[OK]", "Got ignored matches!");
        this.ignoredMatchesList = matchList;
        ignoredListContent.setAdapter(new IgnoredMatchesListAdapter(this, this.ignoredMatchesList));

        this.loadingMatches.setVisibility(View.GONE);
    }
}