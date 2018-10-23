package com.gdziejestmecz.gdzie_jest_mecz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gdziejestmecz.gdzie_jest_mecz.components.EventListItem;
import com.gdziejestmecz.gdzie_jest_mecz.constants.Colors;
import com.gdziejestmecz.gdzie_jest_mecz.models.EventData;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.GoogleMap;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private TextView userFirstnameLabel, userEmailLabel;
    private ImageView userAvatarImageView;
    private GoogleApiClient googleApiClient;

    private DrawerLayout drawerLayout;
    private NavigationView sideBar;
    private Button plusBtn, menuBtn;
    private ArrayList<EventData> eventDataList;
    private LinearLayout eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().hide();

        initUIElements();
        addEventListeners();

        initGoogleAuth();

        createFakeEvents();
        renderEvents();
    }

    private void renderEvents() {
        //cala ta metoda bedzie przepisana, wiec izi
        int count = 0;
        for (EventData eventData : eventDataList) {
            CardView eventCard = new CardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(15, 5, 15, 5);
            eventCard.setLayoutParams(params);

            LinearLayout LLMainWrapper = new LinearLayout(this);
            LLMainWrapper.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout LLDateTimeWrapper = new LinearLayout(this);
            LinearLayout.LayoutParams paramsLLDateTimeWrapper = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LLDateTimeWrapper.setOrientation(LinearLayout.VERTICAL);
            paramsLLDateTimeWrapper.width = 100;
            paramsLLDateTimeWrapper.height = 100;
            paramsLLDateTimeWrapper.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

            LLDateTimeWrapper.setLayoutParams(paramsLLDateTimeWrapper);

            TextView nowLabel = new TextView(this);
            nowLabel.setText("Teraz");
            nowLabel.setWidth(80);
            nowLabel.setTextColor(Colors.boneWhite);
            nowLabel.setBackgroundColor(Colors.wineRed);
            nowLabel.setGravity(Gravity.CENTER);

            TextView date = new TextView(this);
            date.setText("09.10");
            date.setTextColor(Colors.lightGray);
            date.setGravity(Gravity.CENTER);

            TextView time = new TextView(this);
            time.setText("19:20");
            time.setTextColor(Colors.blackyBlack);
            time.setTextSize(24);
            time.setGravity(Gravity.CENTER);

            Boolean eventHappeningNow = count == 0 ? true : false;
            if (eventHappeningNow) {
                LLDateTimeWrapper.addView(nowLabel);
            }

            LLDateTimeWrapper.addView(date);
            LLDateTimeWrapper.addView(time);

            LinearLayout LLTeamsAndLocation = new LinearLayout(this);
            LLTeamsAndLocation.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams paramsLLTeamsAndLocation = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsLLTeamsAndLocation.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
            LLTeamsAndLocation.setLayoutParams(paramsLLTeamsAndLocation);

            LinearLayout LLLocationInfo = new LinearLayout(this);
            LLTeamsAndLocation.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams paramsLLLocationInfo = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsLLLocationInfo.gravity = Gravity.CENTER_HORIZONTAL;
            LLLocationInfo.setLayoutParams(paramsLLLocationInfo);

            ImageView markerIco = new ImageView(this);
            markerIco.setMaxWidth(8);
            markerIco.setMaxHeight(8);
            markerIco.setImageResource(R.drawable.ic_marker);

            TextView pubName = new TextView(this);
            pubName.setText(eventData.pubName);
            pubName.setTextColor(Colors.blackyBlack);
            pubName.setGravity(Gravity.CENTER);

            LLLocationInfo.addView(markerIco);
            LLLocationInfo.addView(pubName);

            LinearLayout LLTeamsInfo = new LinearLayout(this);
            LinearLayout.LayoutParams paramsLLTeamsInfo = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsLLTeamsInfo.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
            LLTeamsInfo.setLayoutParams(paramsLLTeamsInfo);

            ImageView teamOneLogo = new ImageView(this);
            LinearLayout.LayoutParams logoConstraints = new LinearLayout.LayoutParams(40, 40);
            teamOneLogo.setLayoutParams(logoConstraints);

            teamOneLogo.setBackgroundResource(R.drawable.lfc_logo);

            ImageView teamTwoLogo = new ImageView(this);
            teamTwoLogo.setLayoutParams(logoConstraints);
            teamTwoLogo.setBackgroundResource(R.drawable.logo_real_madryt);

            TextView teams = new TextView(this);
            teams.setText(eventData.teamOneName + " - " + eventData.teamTwoName);
            teams.setGravity(Gravity.CENTER);
            teams.setTextColor(Colors.blackyBlack);
            teams.setTextSize(15);

            LLTeamsInfo.addView(teamOneLogo);
            LLTeamsInfo.addView(teams);
            LLTeamsInfo.addView(teamTwoLogo);

            LLTeamsAndLocation.addView(LLLocationInfo);
            LLTeamsAndLocation.addView(LLTeamsInfo);

            LLMainWrapper.addView(LLDateTimeWrapper);
            LLMainWrapper.addView(LLTeamsAndLocation);

            eventCard.addView(LLMainWrapper);
            eventsList.addView(eventCard);

            count++;
        }
    }

    private void createFakeEvents() {
        eventDataList = new ArrayList<EventData>();

        EventData match1 = new EventData("RKS Chuwdu", "JBC Falubas", "Speluno", LocalDateTime.now());

        eventDataList.add(match1);
        eventDataList.add(match1);
        eventDataList.add(match1);
        eventDataList.add(match1);
    }

    private void addEventListeners() {
        plusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainScreen.this, "@string/plus_tapped", Toast.LENGTH_SHORT).show();
            }
        });
        menuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainScreen.this, "@string/menu_tapped", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void initUIElements() {
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.sideBar = findViewById(R.id.sideNav);
        this.plusBtn = findViewById(R.id.plus_btn);
        this.menuBtn = findViewById(R.id.menuBtn);
        this.eventsList = findViewById(R.id.eventsList);

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

}
