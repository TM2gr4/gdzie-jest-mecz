package com.gdziejestmecz.gdzie_jest_mecz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

public class SignInScreen extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE = 777;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private ImageView btn_google, btn_fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        initUIElements();
        initGoogleAuth();
        addEventListeners();

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

    private void addEventListeners() {
        btn_google.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,777);
            }
        });
    }

    private void initUIElements() {
        this.btn_google = findViewById(R.id.btn_google);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            GoogleSignInResult gsiResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(gsiResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult gsiResult) {
        boolean isSuccess = gsiResult.isSuccess();
        if(gsiResult.isSuccess()){
            goMainScreen();
        } else {
            Toast.makeText(this, "Error: CODE " + gsiResult.getStatus(), Toast.LENGTH_LONG).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
