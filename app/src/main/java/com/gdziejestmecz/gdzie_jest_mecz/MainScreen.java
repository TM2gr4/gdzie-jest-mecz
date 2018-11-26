package com.gdziejestmecz.gdzie_jest_mecz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import android.support.design.widget.NavigationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gdziejestmecz.gdzie_jest_mecz.components.EventListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.PostEvent;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.AsyncEventListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.AsyncMatchListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.RetrieveEvents;
import com.gdziejestmecz.gdzie_jest_mecz.models.Event;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;
import com.gdziejestmecz.gdzie_jest_mecz.models.Team;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, AsyncMatchListResponse, AsyncEventListResponse, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private TextView userFirstnameLabel, userEmailLabel;
    private ImageView userAvatarImageView;
    private GoogleApiClient googleApiClient;

    private DrawerLayout drawer_layout;
    private NavigationView sideBar;
    private Button plusBtn, menuBtn;
    private ArrayList<Event> eventList;
    private ActionBarDrawerToggle mToogle;

    private ListView eventsListContent;

    private View addEventPanel;
    private EditText input_matchId, input_pubId, input_desc;
    private Button addEventButton;
    private Button closeAddEventPanel;
    private MapViewFragment mapViewFragment;

    private View addPubPanel;
    private EditText input_pub_name;
    private Button addPubButton;
    private Button closeAddPubPanel;

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
                PopupMenu popup = new PopupMenu(MainScreen.this, plusBtn);
                popup.getMenuInflater()
                        .inflate(R.menu.popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Dodaj Mecz")) {
                            slidePanel(addEventPanel);
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
               // Toast.makeText(MainScreen.this, "@string/menu_tapped", Toast.LENGTH_SHORT).show();
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });

        closeAddEventPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "closing addEventPanel", Toast.LENGTH_SHORT).show();
                slidePanel(addEventPanel);
            }
        });
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "adding event", Toast.LENGTH_SHORT).show();
                prepareEventToAdd();
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
                prepareEventToAdd();
            }
        });
    }

    private void prepareEventToAdd() {

        int matchId = Integer.parseInt(input_matchId.getText().toString());
        int pubId = Integer.parseInt(input_pubId.getText().toString());
        String desc = input_desc.getText().toString();

        Team team = new Team(999, "ZC Bulwy", "https:///Dsada");

        Match match = new Match(matchId, team, team, "2012-12-10", "14:10");
        Pub pub = new Pub(pubId, "Pod ostryga", "to jest adres");
        Event event = new Event(999, match, pub, 0, 1.1, 1.1, desc);

        PostEvent postEvent = new PostEvent(event);
        postEvent.delegate = this;

        postEvent.execute();
    }

    private void initUIElements() {
        this.drawer_layout = findViewById(R.id.drawer_layout);
        this.sideBar = findViewById(R.id.nav_view);

        this.addEventPanel = findViewById(R.id.add_event_panel);
        this.input_matchId = findViewById(R.id.input_matchId);
        this.input_pubId = findViewById(R.id.input_pubId);
        this.input_desc = findViewById(R.id.input_desc);
        this.addEventButton = findViewById(R.id.add_btn_add_event_panel);
        this.closeAddEventPanel = findViewById(R.id.x_btn_add_event_panel);

        this.addPubPanel = findViewById(R.id.add_pub_panel);
        this.input_matchId = findViewById(R.id.input_pub_name);
        this.addPubButton = findViewById(R.id.add_btn_add_pub_panel);
        this.closeAddPubPanel = findViewById(R.id.x_btn_add_pub_panel);


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

    private void signOut() {

        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
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
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.up_slide);

            view.startAnimation(bottomUp);
            view.setVisibility(View.VISIBLE);
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_slide);

            view.startAnimation(bottomDown);
            view.setVisibility(View.GONE);
        }
    }

    private boolean isAddEventPanelShown() {
        return addEventPanel.getVisibility() == View.VISIBLE || addPubPanel.getVisibility() == View.VISIBLE;
    }

    private void clearEventList(){
        eventList = new ArrayList<Event>();

        eventsListContent.setAdapter(null);
    }
    @Override
    public void retrieveMatchesProcessFinished(ArrayList<Event> eventList) {
        this.eventList = eventList;
//        FAKE EVENTS
        /*eventList = new ArrayList<MatchData>();
        Team sampleHomeTeam = new Team(0, "RKS Offline", "httpsDupa:///");
        Team sampleAwayTeam = new Team(1, "JBC Noapi", "httpsDupa:///");

        MatchData match1 = new MatchData(0, sampleHomeTeam, sampleAwayTeam, "12-10-2018", "12:10");
        MatchData match2 = new MatchData(1, sampleAwayTeam, sampleHomeTeam, "12-10-2018", "12:10");
        MatchData match3 = new MatchData(2, sampleHomeTeam, sampleAwayTeam, "12-10-2018", "12:10");
        MatchData match4 = new MatchData(3, sampleAwayTeam, sampleHomeTeam, "12-10-2018", "12:10");

        eventList.add(match1);
        eventList.add(match2);
        eventList.add(match3);
        eventList.add(match4);
        eventList.add(match4);
        eventList.add(match4);
        eventList.add(match4);
        eventList.add(match4);
        eventList.add(match4);
        eventList.add(match4);
*/
        eventsListContent.setAdapter(new EventListAdapter(this, this.eventList));
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

    @SuppressLint("StaticFieldLeak")
    public void searchLocation(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.searchText);
        final String location = locationSearch.getText().toString();

        if(getSupportFragmentManager().findFragmentByTag("fragmentMap")!=null) {
            mapViewFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag("fragmentMap");
        }

        if(location != null && !location.equals("")) {
            new AsyncTask<Void, Void, List<Address>>() {
                @Override
                protected List<Address> doInBackground(Void... voids) {
                    List<Address> addressList = null;

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return addressList;
                }

                public void onPostExecute(List<Address> addressList) {
                    Address address;
                    if (addressList != null && addressList.size() > 0) {
                        address = addressList.get(0);
                        mapViewFragment.drawMarker(new LatLng(address.getLatitude(), address.getLongitude()), location);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nie znaleziono podanego miejsca", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    @Override
    public void addEventProcessFinished(boolean success) {
        if(success) {
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
            slidePanel(addEventPanel);
            clearEventList();
            renderEventList();
        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.sign_out_label) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
