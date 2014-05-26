package com.androidmpgtracker.data;

public class Method {
    //Edmunds api methods
    public static final String GET_MAKES_FOR_YEAR = "/makes";
    public static final String GET_MODEL_FOR_YEAR_AND_MAKE = "/models";

    //MpgTracker Api Methods
    public static final String SAVE_FILL_UP_BASE = "/fillups.php";
    public static final String SAVE_FILL_UP_METHOD = "logMpg";
    public static final String GET_COMMUNITY_MPG = "/fillups.php?method=getCommunityMpg";
}
