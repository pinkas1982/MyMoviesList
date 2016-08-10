package com.myapps.mymovieslist.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapps.mymovieslist.R;
import com.myapps.mymovieslist.constants.Constants;
import com.myapps.mymovieslist.db.MovieDbHandler;
import com.myapps.mymovieslist.movie_object.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by פנקס on 25/02/2016.
 */
public class EditMovieListActivity extends AppCompatActivity {

    public static final String SINGLE_MOVIE_URL = "http://www.omdbapi.com/?i=";


    private TextView subjectText;
    private EditText movieName;
    private  EditText bodyEdit;
    private  EditText urlEdit;
    private Button okButton;
    private  Button cancelButton;
    private  Button showButton;
    private ImageView movieImage;
    private Movie editMovie;
    private int action;
    private String name;
    private String description;
    private String urlMovie;
    private Movie selectedMovie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        initComponent();
        action = getIntent().getIntExtra(Constants.ACTIVITY_KEY, 0);

        switch (action) {
            case (MovieMainActivity.OFF_LINE_CODE):
                Object movieObj = getIntent().getSerializableExtra(Constants.KEY_TITLE_MOVIE);
                if (movieObj != null) {
                    editMovie = (Movie) getIntent().getSerializableExtra(Constants.KEY_TITLE_MOVIE);
                    populateMovie();
                    //  initMovieEdit();

                }

                break;
            case (MovieMainActivity.ON_LINE_CODE):
                Object movieWebObj = getIntent().getSerializableExtra(Constants.KEY_TITLE_MOVIE);
                if (movieWebObj != null) {
                    editMovie =(Movie) movieWebObj;
                    populateMovieFromWeb();
                }
                return;
            default:
                return;

        }
    }




    public void initComponent(){
        subjectText= (TextView)findViewById(R.id.subjectTextViewId);
        movieName= (EditText)findViewById(R.id.MovieNameEditTextId);
        bodyEdit= (EditText)findViewById(R.id.bodyEditTextId);
        okButton = (Button)findViewById(R.id.okButtonId);
        cancelButton = (Button)findViewById(R.id.cancelButtonId);
        showButton = (Button)findViewById(R.id.showButtonId);
        movieImage = (ImageView)findViewById(R.id.movieImageViewId);
        urlEdit = (EditText)findViewById(R.id.urlEditTextId);

    }

    private void populateMovie() {
        name = editMovie.getMovieSubject().toString();
        description = editMovie.getMovieBody().toString();
        urlMovie = editMovie.getMovieUrl().toString();

        movieName.setText(name);
        bodyEdit.setText(description);
        urlEdit.setText(urlMovie);

    }
    private void populateMovieFromWeb() {
        name = editMovie.getMovieSubject().toString();
        description = editMovie.getMovieBody().toString();
        urlMovie = editMovie.getMovieUrl().toString();
        movieName.setText(name);
        bodyEdit.setText(description);
        urlEdit.setText(urlMovie);
    }

    public void addOrUpdateMovie (View v) {
        switch (action){
            case (Constants.NEW_MOVIE):
                addMovieToMovieList();
                break;

            case (Constants.OFFLINE_MOVIE):
                editMovie.setMovieSubject(movieName.getText().toString());
                editMovie.setMovieBody(bodyEdit.getText().toString());
                editMovie.setMovieUrl(urlEdit.getText().toString());
                updateMovieToMovieList(editMovie);
                break;
            case (Constants.WEB_MOVIE):
                addMovieToMovieList();
            default:
                return;

        }
    }

    public void addMovieToMovieList (){
        Intent movieIntent = new Intent(getApplicationContext(), MovieMainActivity.class);
        Movie movie = new Movie(0,movieName.getText().toString(),bodyEdit.getText().toString(),urlEdit.getText().toString(),"",0,"",0.0);
        MovieDbHandler movieDbHandler = new MovieDbHandler(this);
        movieDbHandler.addMovie(movie);
        String movieSubject = movieName.getText().toString();
        movieIntent.putExtra(Constants.ACTIVITY_KEY, movieSubject);
        setResult(Activity.RESULT_OK, movieIntent);
        movieIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(movieIntent);
        finish();
    }

    public void updateMovieToMovieList (Movie movie){
        Intent movieIntent = getIntent();
        //Movie updatemovie = new Movie(,movieName.getText().toString(),bodyEdit.getText().toString(),urlEdit.getText().toString());
        MovieDbHandler movieDbHandler = new MovieDbHandler(this);
        movieDbHandler.updateMovieList(movie);
        movieIntent.putExtra(Constants.ACTIVITY_KEY, movie );
        setResult(Activity.RESULT_OK, movieIntent);
        finish();
    }
//when i click these button, i return to main activity
    public void cancelAndReturnToMainActivity (View v){
        finish();
    }

    //when i click these button, the poster of the movie will be show
    public void showPosterOfMovie (View v){
        String showPoster = urlEdit.getText().toString();
        RefreshTask posterShow = new RefreshTask();
        posterShow.execute(showPoster);
    }


    class RefreshTask extends AsyncTask<String, Void, String> {
        Bitmap bitmap;
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            // do this first (UI Thread)
            //	TextView textOutput = (TextView) findViewById(R.id.text_output);
            //	textOutput.setText("loading...");
            super.onPreExecute();
            dialog = ProgressDialog.show(EditMovieListActivity.this, "Title",
                    "Message");
        }

        @Override
        protected String doInBackground(String... params) {
            // do this on the task thread:

            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();

            try {

                String queryString = "";
               /* queryString += "?list=" + URLEncoder.encode(LIST_TITLE, "utf-8");
                queryString += "&limit=" + RESULTS_LIMIT;*/

                // prepare a URL object :
                URL url = new URL(params[0] + queryString);

                // open a connection
                connection = (HttpURLConnection) url.openConnection();

                // check the result status of the conection:
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    // not good.
                    return null;
                }
                InputStream in = (InputStream) url.getContent();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                // close the stream if it exists:
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // close the connectin if it exists:
                if (connection != null) {
                    connection.disconnect();
                }
            }

            // get the string from the string builder:
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // do this last (with the result from the doInBackgroud() )
            // (UI thread)

            super.onPostExecute(result);

            if (dialog != null)
                dialog.dismiss();

            ImageView myimg= (ImageView) findViewById(R.id.movieImageViewId);
            myimg.setImageBitmap(bitmap);


        }
    }

}

