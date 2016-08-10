package com.myapps.mymovieslist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.myapps.mymovieslist.R;
import com.myapps.mymovieslist.movie_object.Movie;

import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter extends ArrayAdapter<Movie> implements Filterable{
    List<Movie> movieList;
    List<Movie> resultList;
    MoviesAdapter movieListAdapter;
    ListView listFromMain;
    Context c;


    public MoviesAdapter(Context context, int resource, List<Movie> movieList , ListView listFromMain) {
        super(context, resource, movieList);
        this.movieList = movieList;
        movieListAdapter=this;
       this.listFromMain=listFromMain;
        c= context;
       }

    public MoviesAdapter(Context context, int resource, List<Movie> movieList ) {
        super(context, resource, movieList);
        this.movieList = movieList;
        movieListAdapter=this;

    }

    


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View customeMovieList = convertView;

        if (customeMovieList == null) {
            LayoutInflater movieInfater = LayoutInflater.from(getContext());
            customeMovieList = movieInfater.inflate(R.layout.movie_layout, null);

        }
        Movie singelMovieItem = getItem(position);

        TextView movieTitleTextView = (TextView) customeMovieList.findViewById(R.id.movieTextViewId);
        // ImageButton addMovie = (ImageButton)customeMovieList.findViewById(R.id.addImageButtonId);

        // populate the Text with data from the Movie
        movieTitleTextView.setText(singelMovieItem.getMovieSubject());


        return customeMovieList;


    }




    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                resultList = (List<Movie>) results.values;


                notifyDataSetChanged();
                movieListAdapter = new MoviesAdapter(c, R.layout.movie_layout,resultList);
                listFromMain.setAdapter(movieListAdapter);

            }

            @Override
            protected android.widget.Filter.FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Movie> FilteredArrayNames = new ArrayList<Movie>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < movieList.size(); i++) {
                    String dataNames = (movieList.get(i)).getMovieSubject();
                    if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(movieList.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }

}
