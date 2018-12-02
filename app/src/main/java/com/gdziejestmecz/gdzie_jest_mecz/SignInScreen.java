package com.gdziejestmecz.gdzie_jest_mecz;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gdziejestmecz.gdzie_jest_mecz.utils.api.AsyncPostGoogleTokenResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.GoogleTokenResponse;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.PostGoogleToken;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.TokenStore;
import com.gdziejestmecz.gdzie_jest_mecz.utils.api.TokenType;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

public class SignInScreen extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, AsyncPostGoogleTokenResponse {

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
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("access_token", "");
        Log.d("SAVED TOKEN", token);
        TokenStore.setRefreshToken(token);
        if(token!=""){
            goMainScreen(false);
        }

    }

    private void initGoogleAuth() {
        GoogleSignInOptions gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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
                startActivityForResult(intent, 777);
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

        if (requestCode == REQUEST_CODE) {
            GoogleSignInResult gsiResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(gsiResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult gsiResult) {
        boolean isSuccess = gsiResult.isSuccess();
        if (gsiResult.isSuccess()) {
            //TODO wysłać na backend i przeprowadzić autoryzację
            PostGoogleToken postGoogleToken = new PostGoogleToken(gsiResult.getSignInAccount().getIdToken(), TokenType.GOOGLE_TOKEN);
            postGoogleToken.delegate = this;
            postGoogleToken.execute();
        } else {
            Toast.makeText(this, "Error: CODE " + gsiResult.getStatus(), Toast.LENGTH_LONG).show();
        }
    }

    private void goMainScreen(boolean isLoggedManualy) {
        Intent intent = new Intent(this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isLoggedManually", isLoggedManualy);
        startActivity(intent);
    }

    @Override
    public void postGoogleTokenProcessFinished(GoogleTokenResponse token) {
        TokenStore.setAccessToken(token.getAccessToken());
        Log.d("LOGIN RESPONSE" , token.getAccessToken() + " " + token.getRefreshToken());
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("access_token", token.getRefreshToken()).apply();
        goMainScreen(true);
    }
}
