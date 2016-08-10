package com.myapps.mymovieslist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.myapps.mymovieslist.R;
import com.myapps.mymovieslist.adapter.MoviesAdapter;
import com.myapps.mymovieslist.constants.Constants;
import com.myapps.mymovieslist.db.MovieDbHandler;
import com.myapps.mymovieslist.movie_object.Movie;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by פנקס on 25/02/2016.
 */
public class MovieMainActivity extends Activity implements AdapterView.OnItemClickListener {
        //TODO i need to check if i put OFFLINE instead of OFF_LINE what will happend
    public static final int OFF_LINE_CODE = 2;
    public static final int ON_LINE_CODE = 3;

    Movie movie ;
    Movie editMovie;

    Intent movieIntent;
    ImageButton addMovie;
    ImageButton setting;
    ListView movieListView;
    List<Movie> movieList;
    MoviesAdapter movieListAdapter;
    MovieDbHandler movieDb;
    AdapterView.AdapterContextMenuInfo movieInfo;
    EditText movieSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);

        movieDb = new MovieDbHandler(this);
        initComponent();
        registerForContextMenu(movieListView);

    }

    public void initComponent() {
        addMovie = (ImageButton)findViewById(R.id.addMovieImageButtonId);
        //  setting = (ImageButton)findViewById(R.id.settingImageButtonId);
        movieListView = (ListView) findViewById(R.id.movieListViewId);
        movieList = new ArrayList<Movie>();
        movieList = getAllMovies();
        Log.e("TEST", "movie list contains " + movieList.size() + " movies");
        movieListAdapter = new MoviesAdapter(this, R.layout.movie_layout, movieList, movieListView);
        movieListView.setAdapter(movieListAdapter);
        movieListView.setOnItemClickListener(this);
        movieSearch = (EditText)findViewById(R.id.searchMoviesEditTextId);
        movieSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // When user changed the Text
                MovieMainActivity.this.movieListAdapter.getFilter().filter(movieSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//sorting the list by ALPHABETIC order
        movieListAdapter.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getMovieSubject().compareTo(rhs.getMovieSubject());

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater movieMenuInflater = getMenuInflater();
        movieMenuInflater.inflate(R.menu.movie_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        movieInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit_id:
                editMovie(movieInfo.position);
                return true;
            case R.id.delet_id:
                deletedMovie(movieInfo.position);
                return true;
            default:
                return super.onContextItemSelected(item);


        }

    }

    public void refreshList (){
      //  movieListAdapter.notifyDataSetChanged();
        movieList = getAllMovies();
        movieListAdapter = new MoviesAdapter(this, R.layout.movie_layout,movieList);
        movieListView.setAdapter(movieListAdapter);

    }


    public void addMovie (View v){
        PopupMenu popup = new PopupMenu(this, addMovie);
        popup.getMenuInflater().inflate(R.menu.movie_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.addManualMovieId):
                        initiateOffLineActivity();
                        return true;
                    case (R.id.webId):
                        initiateWebActivity();
                }
                return true;
            }


        });
        popup.show();


    }

    public void initiateOffLineActivity (){
        movieIntent = new Intent(this, EditMovieListActivity.class);
        movieIntent.putExtra(Constants.ACTIVITY_KEY, Constants.NEW_MOVIE);
        startActivityForResult(movieIntent, OFF_LINE_CODE);
    }


    public void initiateWebActivity (){
        Intent movieSearchIntent = new Intent(this, SearchMovieActivity.class);
        startActivityForResult(movieSearchIntent, ON_LINE_CODE);
    }

    public void deletedMovie (int position){

        Movie movieToDelete;
        movieToDelete = movieList.remove(position);
        movieListAdapter.notifyDataSetChanged();
        movieDb = new MovieDbHandler(this);
        movieDb.deleteMovie(movieToDelete);

    }

    public void editMovie (int position){
        Movie selectedMovie = movieList.get(position);
        Intent movieIntent = new Intent(this, EditMovieListActivity.class);
        movieIntent.putExtra(Constants.KEY_TITLE_MOVIE, selectedMovie);
        movieIntent.putExtra(Constants.ACTIVITY_KEY, Constants.OFFLINE_MOVIE);
        startActivityForResult(movieIntent, Constants.OFFLINE_MOVIE);

    }

    //Every time I click on an item in the list view it's move to the EditMovieActivity
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            editMovie(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case (Constants.NEW_MOVIE):
                    refreshList();
                    break;
                case (Constants.OFFLINE_MOVIE):
                    refreshList();
                    break;
                case (Constants.WEB_MOVIE):
                    refreshList();
                    break;
                default:
                    return;
            }


        }

    }



    private List<Movie> getAllMovies() {
        Log.e("TEST", "inside getMovies");
        return movieDb.getAllmovies();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exit_or_delet_menu, menu);
        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

// Handle item selection
        switch (item.getItemId()) {
            case R.id.deletAllMoviesId:
                new AlertDialog.Builder(this)
                        .setTitle("Delete Movies")
                        .setMessage("Are you sure you want to delete all movies?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deletAllMovies();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            case R.id.exitId:
               finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deletAllMovies() {
        movieDb = new MovieDbHandler(this);
        movieDb.deletAllMovies();
        refreshList();
    }


}

