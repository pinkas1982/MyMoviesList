package com.myapps.mymovieslist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.myapps.mymovieslist.db.MovieDbConstants.*;


/**
 * Created by פנקס on 25/02/2016.
 */
public class MovieDbhelper extends SQLiteOpenHelper {


    public MovieDbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(MovieDbConstants.LOG_TAG, "Creating All The Tables");

        String CREATE_MOVIES_TABLE = "CREATE TABLE " + MOVIES_TABLE_NAME +
                "(" + MOVIE_ID + " INTEGER PRIMARY KEY autoincrement,"
                    + MOVIE_SUBJECT + " TEXT,"
                    + MOVIE_BODY + " TEXT,"
                    + MOVIE_URL + " TEXT,"
                    + MOVIE_IMDBID + " TEXT,"
                    + MOVIE_YEAR + " INTEGER,"
                    + MOVIE_POSTER + " TEXT,"
                    + MOVIE_RATING + " TEXT)";

        try {
            db.execSQL(CREATE_MOVIES_TABLE);
        }
        catch (SQLiteException ex) {
            Log.e(LOG_TAG, "Create table exception: " +
                    ex.getMessage());

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
