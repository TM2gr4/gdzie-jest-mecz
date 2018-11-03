package com.gdziejestmecz.gdzie_jest_mecz.constants;

public class ServerInfo {
    private static final String SERVER_IP = "212.191.92.88";
    private static final String PORT = "51031";
    private static final String ENTRY_POINT = "/api";

    private static final String ROOT_URL = SERVER_IP + ":" + PORT + ENTRY_POINT;

    public String getRootUrl(){ return ROOT_URL;}

}
