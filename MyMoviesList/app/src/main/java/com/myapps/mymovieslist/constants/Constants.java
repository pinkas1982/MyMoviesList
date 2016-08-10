package com.myapps.mymovieslist.constants;

/**
 * Created by פנקס on 25/02/2016.
 */
public class Constants {
    public static final String KEY_TITLE_MOVIE = "titleMovie";

    public static final String ACTIVITY_KEY = "activity";//when i preform ok button retrive type of the 3 following activity

    public static final int NEW_MOVIE=1;// no movie exist, add new movie to db
    public static final int OFFLINE_MOVIE =2; //movie from db--> update db
    public static final int WEB_MOVIE=3;// movie from Internet --> push to db


    public static final String GET_MOVIE ="movieEdit";
}
