package com.example.alexandra.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.alexandra.popularmovies.BuildConfig;
import com.example.alexandra.popularmovies.fragments.MovieDetailFragment;
import com.example.alexandra.popularmovies.R;
import com.example.alexandra.popularmovies.models.Movie;
import com.example.alexandra.popularmovies.models.Video;
import com.example.alexandra.popularmovies.network.MainApi;
import com.example.alexandra.popularmovies.network.responses.VideosListResponse;
import com.example.alexandra.popularmovies.utils.Constants;
import com.example.alexandra.popularmovies.utils.ListSelectorDialog;
import com.google.inject.Inject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.activity.RoboActionBarActivity;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieGridActivity}.
 */
public class MovieDetailActivity extends RoboActionBarActivity {
    public static final String TAG = "MovieDetailActivity";
    private Movie   movie;
    @Inject
    public  MainApi mainApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            movie = getIntent().getParcelableExtra(MovieDetailFragment.ARG_ITEM_ID);
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.ARG_ITEM_ID,
                    movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container,
                            fragment)
                    .commit();
        }else {
            movie = savedInstanceState.getParcelable(MovieDetailFragment.ARG_ITEM_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState,
                                    PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState,
                outPersistentState);
        outState.putParcelable(MovieDetailFragment.ARG_ITEM_ID,
                movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_detail,
                menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this,
                        new Intent(this,
                                MovieGridActivity.class));
                return true;
            case R.id.share:
                ListSelectorDialog dlg = new ListSelectorDialog(this, getString(R.string.title_selet_trailer));
                dlg.show(movie.getVideosList(),
                        new ListSelectorDialog.listSelectorInterface() {
                            public void selectorCanceled() {

                            }
                            public void selectedItem(String key,
                                                     String item) {
                                executeShareActions(key,item);
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(movie != null){
            getMovieVideos();
        }
    }

    public void getMovieVideos() {
        mainApi.getMovieVideos(BuildConfig.API_KEY,
                movie.getId(),
                new Callback<VideosListResponse>() {
                    @Override
                    public void success(VideosListResponse videosListResponse,
                                        Response response) {
                        if (videosListResponse.results != null) {
                            ArrayList<Video> videoArrayList = new ArrayList<>();
                            for (Video video : videosListResponse.results) {
                                if (video.getType().equals(Constants.TYPE_VIDEOS_TO_SHOW)) {
                                    videoArrayList.add(video);
                                }
                            }
                            movie.setVideosList(videoArrayList);
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


    public void executeShareActions(String key,
                                    String item) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.msg_share_trailer) + " " +
                        Constants.URL_BASE_PLAY_VIDEO + key
        );

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.title_subject_share_trailer) + " " + item);

        startActivity(Intent.createChooser(intent,
                getString(R.string.title_share_trailer)));




    }

}
