package com.myapps.mymovieslist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.myapps.mymovieslist.R;
import com.myapps.mymovieslist.movie_object.Movie;

import java.util.List;

/**
 * Created by פנקס on 27/02/2016.
 */
public class CustomMovieAdapter extends ArrayAdapter {

    TextView movieName;
    TextView movieReleaseDate;
    ImageView moviePoster;
    RatingBar movieRating;
    private LayoutInflater layoutInflater;

    public CustomMovieAdapter(Context context, int resource,  List<Movie> movieList) {
        super(context, resource, movieList);
        initComponent();

    }

    private void initComponent() {

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View customeMovieList = convertView;

        if (customeMovieList == null) {
            LayoutInflater movieInfater = LayoutInflater.from(getContext());
            customeMovieList = movieInfater.inflate(R.layout.custom_movie_list, null);

        }
        //  Movie singelMovieItem = getItem(position);

        TextView movieTitleTextView = (TextView) customeMovieList.findViewById(R.id.movieTextViewId);
        // ImageButton addMovie = (ImageButton)customeMovieList.findViewById(R.id.addImageButtonId);

        // populate the Text with data from the Movie
     //   movieTitleTextView.setText(singelMovieItem.getMovieSubject());


        return customeMovieList;


    }
}
