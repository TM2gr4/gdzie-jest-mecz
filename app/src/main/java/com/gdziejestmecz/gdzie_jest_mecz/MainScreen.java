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
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gdziejestmecz.gdzie_jest_mecz.components.MatchListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.components.MatchSpinnerListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.components.PubSpinnerListAdapter;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.AsyncMatchListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.AsyncAddEventListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.AsyncPubListResponse;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.RetrieveMatches;
import com.gdziejestmecz.gdzie_jest_mecz.components.api.RetrievePubs;
import com.gdziejestmecz.gdzie_jest_mecz.models.Match;
import com.gdziejestmecz.gdzie_jest_mecz.models.Pub;
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
import java.util.List;

import java.util.ArrayList;

public class MainScreen extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
                                                            AsyncAddEventListResponse,
                                                            AsyncPubListResponse,
                                                            AsyncMatchListResponse {

    private TextView userFirstnameLabel, userEmailLabel;
    private ImageView userAvatarImageView;
    private GoogleApiClient googleApiClient;

    private DrawerLayout drawerLayout;
    private NavigationView sideBar;
    private Button plusBtn, menuBtn;

    private ArrayList<Match> matchList;
    private ArrayList<Pub> pubList;

    private ListView matchListContent;

    private View addEventPanel;
    private EditText input_desc;
    private Spinner input_match, input_pub;
    private Button addEventButton;
    private Button closeAddEventPanel;
    private MapViewFragment mapViewFragment;

    private View addPubPanel;
    private Button addPubButton;
    private Button closeAddPubPanel;

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

        getAndRenderMatches();
        getPubs();
    }

    private void getPubs() {
        RetrievePubs retrievePubs = new RetrievePubs();
        retrievePubs.delegate = this;

        retrievePubs.execute();
    }

    private void getAndRenderMatches() {
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
                signOut();
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

//        PostEvent postEvent = new PostEvent(event);
//        postEvent.delegate = this;
//
//        postEvent.execute();
    }

    private void initUIElements() {
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.sideBar = findViewById(R.id.sideNav);

        this.addEventPanel = findViewById(R.id.add_event_panel);
        this.input_match = findViewById(R.id.input_match);
        this.input_pub = findViewById(R.id.input_pub);
        this.input_desc = findViewById(R.id.input_desc);
        this.addEventButton = findViewById(R.id.add_btn_add_event_panel);
        this.closeAddEventPanel = findViewById(R.id.x_btn_add_event_panel);

        this.addPubPanel = findViewById(R.id.add_pub_panel);
        this.addPubButton = findViewById(R.id.add_btn_add_pub_panel);
        this.closeAddPubPanel = findViewById(R.id.x_btn_add_pub_panel);

        this.plusBtn = findViewById(R.id.plus_btn);
        this.menuBtn = findViewById(R.id.menuBtn);

        this.matchListContent = findViewById(R.id.eventsListContent);

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

    private void clearMatchList() {
        matchListContent.setAdapter(null);
    }

    @Override
    public void retrieveMatchesProcessFinished(ArrayList<Match> matchList) {
        Log.d("[API_CALL]", " Matches retrieve result: " + matchList.toString());
        this.matchList = matchList;
        matchListContent.setAdapter(new MatchListAdapter(this, this.matchList));
        input_match.setAdapter(new MatchSpinnerListAdapter(this, this.matchList));
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
            clearMatchList();
        }
    }


    @Override
    public void retrievePubsProcessFinished(ArrayList<Pub> pubList) {
        this.pubList = pubList;
        input_pub.setAdapter(new PubSpinnerListAdapter(this, this.pubList));
    }
}
