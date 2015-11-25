package com.example.alexandra.popularmovies.network.responses;

import com.example.alexandra.popularmovies.models.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alexandra on 29-09-2015.
 */
public class MoviesListResponse {
    @SerializedName("results")
    ArrayList<Movie> movieArrayList;

    public ArrayList<Movie> getMovieArrayList() {
        return movieArrayList;
    }
}
