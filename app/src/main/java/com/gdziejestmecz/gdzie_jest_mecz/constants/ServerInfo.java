package com.gdziejestmecz.gdzie_jest_mecz.constants;

public class ServerInfo {
    private static final String SERVER_IP = "http://212.191.92.88";
    private static final String PORT = "51031";
    private static final String ENTRY_POINT = "/api";

    private static final String ROOT_URL = SERVER_IP + ":" + PORT + ENTRY_POINT;

    private static final String ENDPOINT_EVENTS = "/events";
    private static final String ENDPOINT_MATCHES = "/matches";
    private static final String ENDPOINT_WATCHED_MATCHES = "/watched";
    private static final String ENDPOINT_TEAMS = "/teams";
    private static final String ENDPOINT_PUBS = "/pubs";

    private static final String ADD = "/add";

    public static String getRootUrl(){ return ROOT_URL;}
    public static String getEndpointEvents(){ return ENDPOINT_EVENTS;}
    public static String getEndpointMatches(){ return ENDPOINT_MATCHES;}
    public static String getEndpointWatchedMatches(){ return ENDPOINT_WATCHED_MATCHES;}
    public static String getEndpointTeams(){ return ENDPOINT_TEAMS;}
    public static String getEndpointPubs(){ return ENDPOINT_PUBS;}

    public static String getAdd() {return ADD;}
}
