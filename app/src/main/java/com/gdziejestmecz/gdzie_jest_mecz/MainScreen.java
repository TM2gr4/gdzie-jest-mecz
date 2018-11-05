package com.gdziejestmecz.gdzie_jest_mecz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gdziejestmecz.gdzie_jest_mecz.components.EventListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.AsyncMatchListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.RetrieveEvents;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class MainScreen extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, AsyncMatchListResponse {

    private GoogleMap mMap;
    private TextView userFirstnameLabel, userEmailLabel;
    private ImageView userAvatarImageView;
    private GoogleApiClient googleApiClient;

    private DrawerLayout drawerLayout;
    private NavigationView sideBar;
    private Button plusBtn, menuBtn;
    private ArrayList<Match> matchList;

    private ListView eventsListContent;

    private View addEventPanel;
    private Button closeAddEventPanel;

    private static final String[] INITIAL_PERMS={
        Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initUIElements();
        addEventListeners();

        initGoogleAuth();

        renderEventList();
    }


    private void renderEventList() {
        RetrieveEvents retrieveEvents = new RetrieveEvents();
        retrieveEvents.delegate = this;

        retrieveEvents.execute();
    }

    private void addEventListeners() {
        plusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainScreen.this, "@string/plus_tapped", Toast.LENGTH_SHORT).show();
                slideAddEventPanelUpDown(addEventPanel);
            }
        });
        menuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainScreen.this, "@string/menu_tapped", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        closeAddEventPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "closing addEventPanel", Toast.LENGTH_SHORT).show();
                slideAddEventPanelUpDown(addEventPanel);
            }
        });
    }

    private void initUIElements() {
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.sideBar = findViewById(R.id.sideNav);

        this.addEventPanel = findViewById(R.id.add_event_panel);
        this.closeAddEventPanel = findViewById(R.id.x_btn_add_event_panel);

        this.plusBtn = findViewById(R.id.plus_btn);
        this.menuBtn = findViewById(R.id.menuBtn);
        this.eventsListContent = findViewById(R.id.eventsListContent);

        View headerLayout = sideBar.getHeaderView(0);
        this.userFirstnameLabel = headerLayout.findViewById(R.id.userFirstnameLabel);
        this.userEmailLabel = headerLayout.findViewById(R.id.userEmailLabel);
        this.userAvatarImageView = headerLayout.findViewById(R.id.userAvatarImageView);
    }

    private void initGoogleAuth() {
        GoogleSignInOptions gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsio)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            fillUserInfo(result);
        }
    }

    private void fillUserInfo(GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();

        userFirstnameLabel.setText(account.getDisplayName());
        userEmailLabel.setText(account.getEmail());

        final Context context = getApplicationContext();
        Glide.with(context).load(account.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(userAvatarImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                userAvatarImageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_label:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Toast.makeText(this, "Signing out....", Toast.LENGTH_SHORT).show();
    }

    public void slideAddEventPanelUpDown(final View view) {
        if (!isAddEventPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.up_slide);

            addEventPanel.startAnimation(bottomUp);
            addEventPanel.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_slide);

            addEventPanel.startAnimation(bottomDown);
            addEventPanel.setVisibility(View.GONE);
        }
    }

    private boolean isAddEventPanelShown() {
        return addEventPanel.getVisibility() == View.VISIBLE;
    }

    @Override
    public void retrieveMatchesProcessFinished(ArrayList<Match> matchList) {
        this.matchList = matchList;
//        FAKE EVENTS
        /*matchList = new ArrayList<MatchData>();
        Team sampleHomeTeam = new Team(0, "RKS Offline", "httpsDupa:///");
        Team sampleAwayTeam = new Team(1, "JBC Noapi", "httpsDupa:///");

        MatchData match1 = new MatchData(0, sampleHomeTeam, sampleAwayTeam, "12-10-2018", "12:10");
        MatchData match2 = new MatchData(1, sampleAwayTeam, sampleHomeTeam, "12-10-2018", "12:10");
        MatchData match3 = new MatchData(2, sampleHomeTeam, sampleAwayTeam, "12-10-2018", "12:10");
        MatchData match4 = new MatchData(3, sampleAwayTeam, sampleHomeTeam, "12-10-2018", "12:10");

        matchList.add(match1);
        matchList.add(match2);
        matchList.add(match3);
        matchList.add(match4);
        matchList.add(match4);
        matchList.add(match4);
        matchList.add(match4);
        matchList.add(match4);
        matchList.add(match4);
        matchList.add(match4);
*/
        eventsListContent.setAdapter(new EventListAdapter(this, this.matchList));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MapViewFragment.GPS_SETTINGS){
            if(getSupportFragmentManager().findFragmentByTag("fragmentMap")!=null)
                getSupportFragmentManager().findFragmentByTag("fragmentMap").onActivityResult(requestCode, resultCode, data);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
