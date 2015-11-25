package com.example.alexandra.popularmovies.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.alexandra.popularmovies.R;
import com.example.alexandra.popularmovies.activities.MovieGridActivity;
import com.example.alexandra.popularmovies.models.Movie;
import com.example.alexandra.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Alexandra on 29-09-2015.
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<Movie> mGridData = new ArrayList<Movie>();

    public MovieGridAdapter(Context mContext, int layoutResourceId, ArrayList<Movie> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.mLayoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<Movie> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieViewHolder holder;
        Movie movie = mGridData.get(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new MovieViewHolder(row, mContext,String.valueOf(position));
            row.setTag(holder);
        } else {
            holder = (MovieViewHolder) row.getTag();
        }

        Picasso.with(mContext)
                .load(Constants.URL_BASE_POSTER  + movie.getPosterPath())
                .into(holder.imageViewMovie);
        return row;
    }

    static class MovieViewHolder {
        ImageView imageViewMovie;

        MovieViewHolder(View itemView, final Context mContext, final String movieId){
            imageViewMovie = (ImageView) itemView.findViewById(R.id.imageViewMovie);
            imageViewMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MovieGridActivity) mContext).onItemSelected(movieId);
                }
            });
        }
    }
}
