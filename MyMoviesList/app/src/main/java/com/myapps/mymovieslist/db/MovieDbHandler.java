package com.myapps.mymovieslist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.myapps.mymovieslist.movie_object.Movie;
import static com.myapps.mymovieslist.db.MovieDbConstants.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by פנקס on 25/02/2016.
 */
public class MovieDbHandler {

    private MovieDbhelper movieDbhelper;

    public MovieDbHandler(Context context) {
        movieDbhelper = new MovieDbhelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //add a new movie to the Db
    public void addMovie(Movie movie) {
        SQLiteDatabase db = movieDbhelper.getWritableDatabase();

        ContentValues newMovieValues = new ContentValues();
        newMovieValues.put(MOVIE_SUBJECT, movie.getMovieSubject());
        newMovieValues.put(MOVIE_BODY, movie.getMovieBody());
        newMovieValues.put(MOVIE_URL, movie.getMovieUrl());
        newMovieValues.put(MOVIE_IMDBID, movie.getMovieImdbId());
        newMovieValues.put(MOVIE_YEAR, movie.getMovieYear());
        newMovieValues.put(MOVIE_POSTER, movie.getMoviePoster());
        newMovieValues.put(MOVIE_RATING, movie.getMovieRating());


        try {
            db.insertOrThrow(MOVIES_TABLE_NAME, null, newMovieValues);
        }catch (SQLiteException ex) {
            Log.e(MovieDbConstants.LOG_TAG, ex.getMessage());
            throw ex;
        } finally {
            db.close();
        }
    }
    //update movie on the Db
    public void updateMovieList(Movie movie) {
        SQLiteDatabase db = movieDbhelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(MOVIE_SUBJECT, movie.getMovieSubject());
            db.update(MOVIES_TABLE_NAME, values, MOVIE_ID + " = " + movie.getMovieId(), null);

        } catch (SQLiteException ex) {
            Log.e(MovieDbConstants.LOG_TAG, ex.getMessage());
            throw ex;
        }
        finally {
            db.close();
        }
    }

    //get all movie from the Db
    public List<Movie> getAllmovies() {
        List<Movie> movieList = new ArrayList<Movie>();
        SQLiteDatabase db = movieDbhelper.getReadableDatabase();

        Cursor cursor = db.query(MOVIES_TABLE_NAME, null, null,
                null, null, null, null, null);


        while (cursor.moveToNext()) {
            int movieId = cursor.getInt(0);
            String movieSubject = cursor.getString(1);
            String movieBody = cursor.getString(2);
            String movieUrl = cursor.getString(3);
            String movieImdbId = cursor.getColumnName(4);
            Integer movieYear = cursor.getInt(5);
            String moviePoster = cursor.getString(6);
            double movieRating = cursor.getDouble(7);

            Movie newMovie = new Movie(movieId, movieSubject, movieBody, movieUrl, movieImdbId,movieYear,moviePoster,movieRating);
            movieList.add(newMovie);

        }
        db.close();
        return movieList;

    }

    // Delete a book from the Db
    public void deleteMovie(Movie movie) {
        SQLiteDatabase db = movieDbhelper.getWritableDatabase();
        try {
            db.delete(MOVIES_TABLE_NAME, MOVIE_ID + "=?",
                    new String[] { String.valueOf(movie.getMovieId()) } );
        } catch (SQLiteException ex) {
            Log.e(MovieDbConstants.LOG_TAG, ex.getMessage());
            throw ex;
        }
        finally {
            db.close();
        }
    }
    //delet all movies from db
    public void deletAllMovies() {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = movieDbhelper.getWritableDatabase();
        db.execSQL("DELETE from " +  MOVIES_TABLE_NAME);
    }
}


