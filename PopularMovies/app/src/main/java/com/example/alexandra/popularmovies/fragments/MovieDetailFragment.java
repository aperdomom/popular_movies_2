package com.example.alexandra.popularmovies.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.alexandra.popularmovies.BuildConfig;
import com.example.alexandra.popularmovies.R;
import com.example.alexandra.popularmovies.models.Movie;
import com.example.alexandra.popularmovies.models.Review;
import com.example.alexandra.popularmovies.models.Video;
import com.example.alexandra.popularmovies.network.MainApi;
import com.example.alexandra.popularmovies.network.responses.ReviewsListResponse;
import com.example.alexandra.popularmovies.network.responses.VideosListResponse;
import com.example.alexandra.popularmovies.utils.Constants;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.fragment.RoboFragment;

public class MovieDetailFragment extends RoboFragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String TAG = "MovieDetailFragment";
    private Movie   movie;
    public LinearLayout trailersLayout;
    public LinearLayout reviewsLayout;
    @Inject
    public  MainApi mainApi;


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            movie = getArguments().getParcelable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail,
                container,
                false);
        TextView valueMovieName = (TextView) rootView.findViewById(R.id.valueMovieName);
        TextView  valueMovieRelease = (TextView) rootView.findViewById(R.id.valueMovieRelease);
        TextView  valueMovieRanting = (TextView) rootView.findViewById(R.id.valueMovieRanting);
        TextView  textViewMovieSynopsis = (TextView)rootView.findViewById(R.id.textViewMovieSynopsis);
        ImageView imageViewMoviePoster = (ImageView) rootView.findViewById(R.id.imageViewMoviePoster);
        trailersLayout = (LinearLayout) rootView.findViewById(R.id.trailersLayout);
        reviewsLayout = (LinearLayout) rootView.findViewById(R.id.reviewsLayout);
        final RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,
                                   MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (movie.isFavorite()) {
                        movie.setFavorite(false);
                        movie.update(getContext());
                        ratingBar.setRating(0);
                    } else {
                        movie.setFavorite(true);
                        movie.update(getContext());
                        ratingBar.setRating(1);
                    }

                }
                return true;
            }
        });

        if(movie!= null){
            valueMovieName.setText(movie.getTitle());
            valueMovieRelease.setText(movie.getReleaseDate());
            valueMovieRanting.setText(String.valueOf(movie.getVoteAverage()));
            textViewMovieSynopsis.setText(movie.getOverview());
            if(movie.isFavorite()){
                ratingBar.setRating(1);
            }
            getMovieVideos();
            getMovieReviews();

            Picasso.with(getActivity())
                    .load(Constants.URL_BASE_POSTER + movie.getPosterPath())
                    .into(imageViewMoviePoster);
            }

            return rootView;
        }


    public void getMovieVideos() {
        mainApi.getMovieVideos(BuildConfig.API_KEY,
                movie.getId(),
                new Callback<VideosListResponse>() {
                    @Override
                    public void success(VideosListResponse videosListResponse,
                                        Response response) {
                        if (videosListResponse.results != null) {
                            for (Video video : videosListResponse.results) {
                                if (video.getType().equals("Trailer")) {
                                    addItemTrailer(video);
                                }
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG,
                                "getMovieVideos" +
                                        error.getMessage());
                    }
                });
    }

    public void addItemTrailer(final Video video){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final LinearLayout itemTrailer = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.item_trailer,
                            null);
                    TextView trailer = (TextView) itemTrailer.findViewById(R.id.trailer);
                    trailer.setText(video.getName());
                    itemTrailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(Constants.URL_BASE_PLAY_VIDEO + video.getKey())));
                        }
                    });
                    trailersLayout.addView(itemTrailer);

                } catch (Exception ex) {
                    Log.e(TAG,
                            ex.getMessage());
                }

            }
        });

    }

    public void getMovieReviews(){
        mainApi.getMovieReviews(BuildConfig.API_KEY,
                movie.getId(),
                new Callback<ReviewsListResponse>() {
                    @Override
                    public void success(ReviewsListResponse reviewsListResponse,
                                        Response response) {
                        if (reviewsListResponse.results != null) {
                            for (Review review : reviewsListResponse.results) {
                                addItemReview(review);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG,
                                "getMovieVideos" +
                                        error.getMessage());
                    }
                });
    }

    public void addItemReview(final Review review){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LinearLayout itemReview = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.item_review,
                            null);
                    TextView author = (TextView) itemReview.findViewById(R.id.authorText);
                    TextView content = (TextView) itemReview.findViewById(R.id.contentText);
                    TextView readMore = (TextView) itemReview.findViewById(R.id.readMoreText);
                    author.setText(review.getAuthor() + ",");
                    content.setText(review.getContent().substring(0,
                            200) + "...");
                    readMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(review.getUrl())));
                        }
                    });
                    reviewsLayout.addView(itemReview);
                } catch (Exception ex) {
                    Log.e(TAG,
                            ex.getMessage());
                }

            }
        });

    }


}
