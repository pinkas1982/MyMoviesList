package com.myapps.mymovieslist.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.myapps.mymovieslist.R;
import com.myapps.mymovieslist.adapter.MoviesAdapter;
import com.myapps.mymovieslist.constants.Constants;
import com.myapps.mymovieslist.movie_object.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import static com.myapps.mymovieslist.constants.MovieKeys.MovieBoxOffice.*;

/**
 * Created by פנקס on 25/02/2016.
 */
public class SearchMovieActivity extends Activity  {

    private MoviesAdapter moviesAdapter;
    public static final String URL = " http://www.omdbapi.com/?s=";
    public static final String SINGLE_MOVIE_URL = "http://www.omdbapi.com/?i=";
    public static final String API_KEY = "&plot=short&r=json";
    public static final String GET_SINGLE_MOVIE = "singleMovie";
    public static final String GET_MOVIE_LIST = "movieList";
    private ProgressDialog progress;
    String movieToGet;


    EditText searchMovie;
    ListView searchMovieList;
    Button movieCancel;
    ImageButton searchButton;
    List<Movie> movieList = new ArrayList<Movie>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        initComponent();



    }

    private void initComponent() {
        searchMovie = (EditText) findViewById(R.id.searchMovieEditTextId);
        searchMovieList = (ListView) findViewById(R.id.MovielistViewId);
        movieCancel = (Button) findViewById(R.id.cancelButtonId);
        moviesAdapter= new MoviesAdapter(this, R.layout.movie_layout, movieList);
        searchButton = (ImageButton)findViewById(R.id.searchImageButtonId);

    }

    public void searchButton(View view) {
        String requestedMovie = searchMovie.getText().toString();
        requestedMovie = requestedMovie.replaceAll(" ", "%20");
        String url = URL + requestedMovie;
        MovieSearch myMovieSearch = new MovieSearch();
        myMovieSearch.execute(url, GET_MOVIE_LIST);
        moviesAdapter.clear();
        waitProgressBar("please wait while loading movie");
    }


    public void waitProgressBar (String message){
        progress = new ProgressDialog(this);
        progress.setTitle("loading");
        progress.setMessage(message);
        progress.show();
    }


    class MovieSearch extends AsyncTask<String, Void, String> implements AdapterView.OnItemClickListener
    {

        @Override
        protected String doInBackground(String... params) {
            String result = sendHttpRequest(params[0]);
            movieToGet = (params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject fullResponse= null;
           if(s == null || s.equals(""))
            {
                Toast.makeText(SearchMovieActivity.this, "Failed to get movies", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                fullResponse = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (movieToGet.equals(GET_MOVIE_LIST)){
                retriveMovieList(fullResponse);
            }else if(movieToGet.equals(GET_SINGLE_MOVIE)){
                retriveMovieByImdbId(fullResponse);
            }
            progress.dismiss();

            }

        private void retriveMovieList(JSONObject fullResponse) {

            try {

                fullResponse = new JSONObject(String.valueOf(fullResponse));
                JSONArray movieResultsArr = fullResponse.getJSONArray(KEY_MOVIE_SEARCH);
                for(int i = 0; i< movieResultsArr.length(); i++){
                    JSONObject currentMovie = movieResultsArr.getJSONObject(i);
                    String movieTitle = currentMovie.getString(KEY_TITLE);
                    String movieImdbId = currentMovie.getString(KEY_IMDBID);

                    Movie movie = new Movie();
                    movie.setMovieSubject(movieTitle);
                    movie.setMovieImdbId(movieImdbId);
                    movieList.add(movie);
                }
                refreshList();

            }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void refreshList() {
            moviesAdapter = new MoviesAdapter(SearchMovieActivity.this, R.layout.movie_layout,movieList);
            searchMovieList.setAdapter(moviesAdapter);
            searchMovieList.setOnItemClickListener(this);

        }


        private void retriveMovieByImdbId(JSONObject fullResponse) {

            try {

                    String movieTitle = fullResponse.getString(KEY_TITLE);
                    String moviePlot = fullResponse.getString(KEY_PLOT);
                    String moviePoster = fullResponse.getString(KEY_POSTER);
                    String movieYear = fullResponse.getString(KEY_YEAR);
                    String movieRating = fullResponse.getString(KEY_RATING);
                    Movie movie = new Movie();
                    movie.setMovieSubject(movieTitle);
                    movie.setMovieBody(moviePlot);
                    movie.setMovieUrl(moviePoster);
                    movie.setMoviePoster(moviePoster);
                    movie.setMovieRating(Double.parseDouble(movieRating));
                    movie.setMovieYear(Integer.parseInt(movieYear));
                    Intent movieIntent = new Intent(SearchMovieActivity.this, EditMovieListActivity.class);
                    movieIntent.putExtra(Constants.KEY_TITLE_MOVIE, movie);
                    movieIntent.putExtra(Constants.ACTIVITY_KEY, Constants.WEB_MOVIE);
                    startActivityForResult(movieIntent, Constants.WEB_MOVIE);


            } catch (JSONException e)
            {
                e.printStackTrace();

            }

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String requstedImdb = movieList.get(position).getMovieImdbId();
            String singleMovieUrl = SINGLE_MOVIE_URL + requstedImdb + API_KEY;
            MovieSearch myMovieSearch = new MovieSearch();
            myMovieSearch.execute(singleMovieUrl, GET_SINGLE_MOVIE);

        }

    }

    public String sendHttpRequest(String urlString) {
        BufferedReader input = null;
        HttpURLConnection httpCon = null;
        StringBuilder response = new StringBuilder();
        try {
           URL url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();
// Check the status of the connection
            if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("TEST", "Cannot connect to: " + urlString);
                return null;
            }
// Use BufferReader to read the data into a string
            input = new BufferedReader(
                    new InputStreamReader(httpCon.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                response.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpCon != null) {
                httpCon.disconnect();
            }
        }
        return response.toString();
    }

    public void cancelAndReturnToMainActivity (View v){
        finish();
    }

}
