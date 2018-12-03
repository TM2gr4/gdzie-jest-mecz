package com.gdziejestmecz.gdzie_jest_mecz;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen.MatchListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen.MatchSpinnerListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.components.mainScreen.PubSpinnerListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;
import com.gdziejestmecz.gdzie_jest_mecz.utils.AsyncLocationFinderResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.LocationFinder;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncMatchListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncPostGoogleTokenResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncPostMatchResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncPubListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.GoogleTokenResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.PostGoogleToken;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.PostMatch;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.RetrieveMatches;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.RetrievePubs;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.TokenStore;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.TokenType;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
                                                            AsyncPubListResponse,
                                                            AsyncMatchListResponse,
                                                            AsyncPostMatchResponse,
                                                            NavigationView.OnNavigationItemSelectedListener,
                                                            AsyncPostGoogleTokenResponse {

    private TextView userFirstnameLabel, userEmailLabel;
    private ImageView userAvatarImageView;
    private GoogleApiClient googleApiClient;

    private MapViewFragment mapViewFragment;
    private DrawerLayout drawer_layout;
    private NavigationView sideBar;
    private Button plusBtn, menuBtn;
    private RelativeLayout loadingMatches;
    private ActionBarDrawerToggle mToogle;

    private ArrayList<Match> matchList;
    private ArrayList<Pub> pubList;

    private ListView matchListContent;

    private View addMatchPanel;
    private EditText input_desc;
    private Spinner input_match, input_pub;
    private Button addMatchButton;
    private Button closeAddMatchPanel;
    private RelativeLayout progressBarAddMatch;

    private View addPubPanel;
    private Button addPubButton;
    private Button closeAddPubPanel;

    private EditText searchBar;
    private Switch searchTypeSwitch;
    private boolean doSearchPubs = false;


    private static final String[] INITIAL_PERMS={
        Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.open, R.string.closed);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initUIElements();
        addEventListeners();
        initGoogleAuth();
        Intent intent = getIntent();
        Boolean isLoggedManually = intent.getExtras().getBoolean("isLoggedManually");
        if(isLoggedManually){
            getAndRenderMatches();
            getPubs();
        }
        else {
            authorize();
        }
    }

    private void getPubs() {
        RetrievePubs retrievePubs = new RetrievePubs();
        retrievePubs.delegate = this;

        retrievePubs.execute();
    }

    private void getAndRenderMatches() {
        this.loadingMatches.setVisibility(View.VISIBLE);

        RetrieveMatches retrieveMatches = new RetrieveMatches();
        retrieveMatches.delegate = this;

        retrieveMatches.execute();
    }


    private void addEventListeners() {
        plusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainScreen.this, plusBtn);
                popup.getMenuInflater()
                        .inflate(R.menu.popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Dodaj Mecz")) {
                            slidePanel(addMatchPanel);
                        } else if (item.getTitle().equals("Dodaj Pub")) {
                            slidePanel(addPubPanel);
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
        menuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });

        closeAddMatchPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidePanel(addMatchPanel);
            }
        });
        addMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "Adding match", Toast.LENGTH_SHORT).show();

                addMatchButton.setEnabled(false);
                closeAddMatchPanel.setEnabled(false);

                prepareMatchToAdd();
            }
        });
        closeAddPubPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "closing addPubPanel", Toast.LENGTH_SHORT).show();
                slidePanel(addPubPanel);
            }
        });
        addPubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "adding pub", Toast.LENGTH_SHORT).show();
            }
        });

        searchTypeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchSwitchAction();
            }
        });
    }

    private void prepareMatchToAdd() {
        progressBarAddMatch.setVisibility(View.VISIBLE);

        Match match = (Match) input_match.getSelectedItem();
        Pub pub = (Pub) input_pub.getSelectedItem();
        String desc = input_desc.getText().toString();

        PostMatch postMatch = new PostMatch(match, pub, desc);
        postMatch.delegate = this;
        postMatch.execute();
    }

    private void initUIElements() {
        this.drawer_layout = findViewById(R.id.drawer_layout);
        this.sideBar = findViewById(R.id.nav_view);
        this.loadingMatches = findViewById(R.id.loading_matches);

        this.addMatchPanel = findViewById(R.id.add_match_panel);
        this.input_match = findViewById(R.id.input_match);
        this.input_pub = findViewById(R.id.input_pub);
        this.input_desc = findViewById(R.id.input_desc);
        this.addMatchButton = findViewById(R.id.add_btn_add_match_panel);
        this.progressBarAddMatch = findViewById(R.id.loadingAddMatch);
        this.closeAddMatchPanel = findViewById(R.id.x_btn_add_match_panel);

        this.addPubPanel = findViewById(R.id.add_pub_panel);
        this.addPubButton = findViewById(R.id.add_btn_add_pub_panel);
        this.closeAddPubPanel = findViewById(R.id.x_btn_add_pub_panel);

        this.plusBtn = findViewById(R.id.plus_btn);
        this.menuBtn = findViewById(R.id.menuBtn);

        this.matchListContent = findViewById(R.id.matchListContent);

        View headerLayout = sideBar.getHeaderView(0);
        this.userFirstnameLabel = headerLayout.findViewById(R.id.userFirstnameLabel);
        this.userEmailLabel = headerLayout.findViewById(R.id.userEmailLabel);
        this.userAvatarImageView = headerLayout.findViewById(R.id.userAvatarImageView);

        this.searchTypeSwitch = findViewById(R.id.searchTypeSwitch);
        this.searchBar = findViewById(R.id.searchText);
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
        Toast.makeText(this, "Nie można nawiązać połacznia...", Toast.LENGTH_SHORT);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("access_token", "").apply();
                        Intent i = new Intent(MainScreen.this,SignInScreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(MainScreen.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public void slidePanel(final View view) {
        if (!isAddEventPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.up_slide);

            view.startAnimation(bottomUp);
            view.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this, R.anim.bottom_slide);

            view.startAnimation(bottomDown);
            view.setVisibility(View.GONE);
        }
    }

    private boolean isAddEventPanelShown() {
        return addMatchPanel.getVisibility() == View.VISIBLE || addPubPanel.getVisibility() == View.VISIBLE;
    }

    @Override
    public void retrieveMatchesProcessFinished(ArrayList<Match> matchList) {
        Log.d("[API_CALL]", " Matches retrieve result: " + matchList.toString());
        this.matchList = matchList;

        matchListContent.setAdapter(new MatchListAdapter(this, getMatchesWithNonZeroPubsCount(this.matchList)));
        input_match.setAdapter(new MatchSpinnerListAdapter(this, this.matchList));
        this.loadingMatches.setVisibility(View.GONE);
    }

    private ArrayList<Match> getMatchesWithNonZeroPubsCount(ArrayList<Match> matchList) {
        ArrayList<Match> filteredMatches = new ArrayList<Match>();
        for (Match m : matchList) {
            if (m.getPubs().size() > 0) {
                filteredMatches.add(m);
            }
        }
        return filteredMatches;
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

    public void handleSearchButton(View view){
        String searchFor = searchBar.getText().toString();
            if (doSearchPubs) {
                searchByPubs(searchFor);
            } else {
                searchByEvents(searchFor);
            }
    }

    public void searchLocation(String location) {
        Object[] dataTransfer = new Object[2];
        dataTransfer[0] = getApplicationContext();
        dataTransfer[1] = location;

        LocationFinder locationFinder = new LocationFinder(new AsyncLocationFinderResponse() {
            @Override
            public void onLocationSeachCompleted(List<Address> addressList) {
                displayLocations(addressList);
            }
        });
        locationFinder.execute(dataTransfer);

    }

    private void displayLocations(final List<Address> incomingAddressList){
        if(incomingAddressList != null && incomingAddressList.size() > 0) {
            String[] extractedAddressNames;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.address_list_popup, viewGroup, false);

            if(getSupportFragmentManager().findFragmentByTag("fragmentMap")!=null) {
                mapViewFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag("fragmentMap");
            }

            extractedAddressNames = new String[incomingAddressList.size()];
            for(int i = 0; i < incomingAddressList.size(); i++){
                extractedAddressNames[i] = incomingAddressList.get(i).getAddressLine(0);
            }

            builder.setTitle("Znalezione miejsca");
            builder.setView(viewInflated);

            builder.setItems(extractedAddressNames,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    LatLng latLng = new LatLng(incomingAddressList.get(item).getLatitude(), incomingAddressList.get(item).getLongitude());
                    mapViewFragment.clearMarkers();
                    mapViewFragment.drawMarker(latLng,incomingAddressList.get(item).getAddressLine(0), true);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else {
            Toast.makeText(getApplicationContext(), "Nie znaleziono podanego miejsca", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrievePubsProcessFinished(ArrayList<Pub> pubList) {
        this.pubList = pubList;
        input_pub.setAdapter(new PubSpinnerListAdapter(this, this.pubList));
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.watched_matches_screen_label) {
            Intent myIntent = new Intent(this.getApplicationContext(), WatchedMatchesScreen.class);
            startActivityForResult(myIntent, 0);
        } else if (id == R.id.ignored_matches_screen_label) {

        } else if (id == R.id.sign_out_label) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void postMatchProcessFinished(boolean isSuccessful) {
        if (isSuccessful) {
            Toast.makeText(this, "Odświeżam liste meczów...", Toast.LENGTH_SHORT).show();
            hideMatchList();
            getAndRenderMatches();
            showMatchList();
            slidePanel(addMatchPanel);
        } else {
            Toast.makeText(this, "Ups, nie mozna dodać meczu :-(", Toast.LENGTH_SHORT).show();
        }
        progressBarAddMatch.setVisibility(View.GONE);
        
        addMatchButton.setEnabled(true);
        closeAddMatchPanel.setEnabled(true);
    }

    private void hideMatchList() {
        matchListContent.setVisibility(View.GONE);
    }
    private void showMatchList() {
        matchListContent.setVisibility(View.VISIBLE);
    }

    private void authorize() {
        PostGoogleToken postGoogleToken = new PostGoogleToken(TokenStore.getRefreshToken(), TokenType.REFRESH_TOKEN);
        postGoogleToken.delegate = this;
        postGoogleToken.execute();
    }

    @Override
    public void postGoogleTokenProcessFinished(GoogleTokenResponse token) {
        TokenStore.setAccessToken(token.getAccessToken());
        Log.d("LOGIN RESPONSE" , token.getAccessToken() + " " + token.getRefreshToken());
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("access_token", token.getRefreshToken()).apply();
        getAndRenderMatches();
        getPubs();
    }

    private void handleSearchSwitchAction(){
        doSearchPubs = searchTypeSwitch.isChecked();
        if(doSearchPubs) {
            searchTypeSwitch.setText(searchTypeSwitch.getTextOn());
        } else {
            searchTypeSwitch.setText(searchTypeSwitch.getTextOff());
        }
    }

    private void searchByEvents(String team){
        ArrayList<Match> filteredMatch = new ArrayList<>();
        if(matchList!=null && !matchList.isEmpty()) {
            if(getSupportFragmentManager().findFragmentByTag("fragmentMap")!=null) {
                mapViewFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag("fragmentMap");
                mapViewFragment.clearMarkers();
            }
            if(!team.equals("")) {
                for (Match match : matchList) {
                    if (match.getAwayTeam().getName().toLowerCase().contains(team.toLowerCase()) || match.getHomeTeam().getName().toLowerCase().contains(team.toLowerCase()))
                        filteredMatch.add(match);
                }
                matchListContent.setAdapter(new MatchListAdapter(this, getMatchesWithNonZeroPubsCount(filteredMatch)));
                input_match.setAdapter(new MatchSpinnerListAdapter(this, filteredMatch));
            } else {
                matchListContent.setAdapter(new MatchListAdapter(this, getMatchesWithNonZeroPubsCount(matchList)));
                input_match.setAdapter(new MatchSpinnerListAdapter(this, matchList));
            }
        }
    }

    private void searchByPubs(String place){
        ArrayList<Match> filteredMatch = new ArrayList<>();
        if(matchList!=null && !matchList.isEmpty()) {
            if(getSupportFragmentManager().findFragmentByTag("fragmentMap")!=null) {
                mapViewFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag("fragmentMap");
                mapViewFragment.clearMarkers();
            }
            if(!place.equals("")) {
                for (Match match : matchList) {
                   for(Pub pub : match.getPubs()){
                       if(pub.getName().toLowerCase().contains(place.toLowerCase())) {
                           filteredMatch.add(match);
                           break;
                       }
                   }
                }
                matchListContent.setAdapter(new MatchListAdapter(this, getMatchesWithNonZeroPubsCount(filteredMatch)));
                input_match.setAdapter(new MatchSpinnerListAdapter(this, filteredMatch));
            } else {
                matchListContent.setAdapter(new MatchListAdapter(this, getMatchesWithNonZeroPubsCount(matchList)));
                input_match.setAdapter(new MatchSpinnerListAdapter(this, matchList));
            }
        }
    }
}
