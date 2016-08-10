package com.myapps.mymovieslist.movie_object;

import java.io.Serializable;

/**
 * Created by פנקס on 25/02/2016.
 */
public class Movie implements Serializable{

    private int movieId;
    private String movieSubject;
    private String movieBody;
    private String movieUrl;
    private String movieImdbId;
    private int movieYear;
    private String moviePoster;
    private double movieRating;


public Movie (){

}



    public Movie(int movieId, String movieSubject, String movieBody, String movieUrl,
                 String movieImdbId,int movieYear,String moviePoster,double movieRating) {

        this.movieId = movieId;
        this.movieSubject = movieSubject;
        this.movieBody = movieBody;
        this.movieUrl = movieUrl;
         this.movieImdbId = movieImdbId;

    }

    public int getMovieId() {

        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieSubject() {
        return movieSubject;
    }

    public void setMovieSubject(String movieSubject) {
        this.movieSubject = movieSubject;
    }

    public String getMovieBody() {
        return movieBody;
    }

    public void setMovieBody(String movieBody) {
        this.movieBody = movieBody;
    }
    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getMovieImdbId() {
        return movieImdbId;
    }

    public void setMovieImdbId(String movieImdbId) {
        this.movieImdbId = movieImdbId;
    }
    public int getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(int movieYear) {
        this.movieYear = movieYear;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }
}




