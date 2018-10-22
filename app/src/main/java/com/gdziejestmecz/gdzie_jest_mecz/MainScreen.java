package com.gdziejestmecz.gdzie_jest_mecz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.GoogleMap;

public class MainScreen extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private TextView userFirstnameLabel, userEmailLabel;
    private ImageView userAvatarImageView;
    private GoogleApiClient googleApiClient;

    private DrawerLayout drawerLayout;
    private NavigationView sideBar;
    private Menu sideMenu;
    private Button plusBtn, menuBtn;

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
        getMenuInflater().inflate(R.menu.side_view, menu);
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
