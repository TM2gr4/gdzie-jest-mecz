package com.gdziejestmecz.gdzie_jest_mecz.utils.api;

public class TokenStore {
    public static String accessToken;
    public static String refreshToken;

    public static void setAccessToken(String accessToken) {
        TokenStore.accessToken = accessToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setRefreshToken(String refreshToken) {
        TokenStore.refreshToken = refreshToken;
    }

    public static String getRefreshToken() {

        return refreshToken;
    }
}
