package com.example.alexandra.popularmovies.network;

import com.example.alexandra.popularmovies.network.responses.MoviesListResponse;
import com.example.alexandra.popularmovies.network.responses.ReviewsListResponse;
import com.example.alexandra.popularmovies.network.responses.VideosListResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Alexandra on 29-09-2015.
 */
public interface MainApi {

    @GET("/movie/popular")
    void getMoviesPopular(@Query("api_key")String apiKey,
                          Callback<MoviesListResponse> callback);

    @GET("/movie/{ID}/images")
    void getMovieImages(@Query("api_key")String apiKey,
                        @Path("ID")String movieId,
                        Callback<MoviesListResponse> callback);

    @GET("/movie/{ID}/videos")
    void getMovieVideos(@Query("api_key")String apiKey,
                          @Path("ID")long movieId,
                        Callback<VideosListResponse> callback);

    @GET("/movie/{ID}/reviews")
    void getMovieReviews(@Query("api_key")String apiKey,
                         @Path("ID")long movieId,
                        Callback<ReviewsListResponse> callback);

}
