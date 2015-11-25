package com.example.alexandra.popularmovies.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.alexandra.popularmovies.data.MovieProvider;
import com.example.alexandra.popularmovies.data.MovieContract.MovieEntry;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alexandra on 29-09-2015.
 */
public class Movie
        implements Parcelable {

    private long    id;
    private String  overview;
    @SerializedName("release_date")
    private String  releaseDate;
    @SerializedName("poster_path")
    private String  posterPath;
    private String  title;
    @SerializedName("vote_average")
    private double voteAverage;
    private double popularity;
    private boolean favorite;
    private ArrayList<Video> videosList;
    private ArrayList<Review> reviewsList;


    public Movie(long id,
                 String overview,
                 String releaseDate,
                 String posterPath,
                 String title,
                 double voteAverage){

        this.id = id;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public ArrayList<Video> getVideosList() {
        return videosList != null? videosList: new ArrayList<Video>();
    }

    public void setVideosList(ArrayList<Video> videosList) {
        this.videosList = videosList;
    }

    public ArrayList<Review> getReviewsList() {
        return reviewsList!= null? reviewsList: new ArrayList<Review>();
    }

    public void setReviewsList(ArrayList<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,
                              int flags) {
        dest.writeLong(id);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
        dest.writeInt(favorite ? 1 : 0);
    }

    private Movie(Parcel in) {
        this.id = in.readLong();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
        this.popularity = in.readDouble();
        this.favorite = in.readInt()==1;
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public Movie(final Cursor cursor) {
        this.id = cursor.getLong(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ID));
        this.overview = cursor.getString(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW));
        this.releaseDate = cursor.getString(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE));
        this.posterPath = cursor.getString(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH));
        this.title = cursor.getString(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE));
        this.voteAverage = cursor.getDouble(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVERAGE));
        this.popularity = cursor.getDouble(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POPULARITY));
        this.favorite = cursor.getInt(
                cursor.getColumnIndex(MovieEntry.COLUMN_NAME_FAVORITE)) == 1;
    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_NAME_ID, this.getId() );
        values.put(MovieEntry.COLUMN_NAME_OVERVIEW, this.getOverview());
        values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, this.getReleaseDate() );
        values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, this.getPosterPath() );
        values.put(MovieEntry.COLUMN_NAME_TITLE, this.getTitle() );
        values.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, this.getVoteAverage());
        values.put(MovieEntry.COLUMN_NAME_POPULARITY, this.getPopularity() );
        values.put(MovieEntry.COLUMN_NAME_FAVORITE,this.isFavorite()?1:0);
        return values;
    }

    public static Cursor getDBMovies(Context context){
        Uri uri = MovieProvider.CONTENT_URI;
        return context.getContentResolver().query(uri,null,null,null,null);
    }

    public static Movie getDBMovie(Context context,long id){
        Uri uri = Uri.parse(MovieProvider.CONTENT_URI+ "/" + String.valueOf(id));
        Cursor cursor = context.getContentResolver().query(uri,null,null,null,null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        if (cursor.moveToFirst()) {
            Movie item = new Movie(cursor);
            cursor.close();
            return  item;
        }

        return null;
    }

    public static ArrayList<Movie> getMovies(Context context) {
        Cursor cursor = getDBMovies(context);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Movie item = new Movie(cursor);
                    movieArrayList.add(item);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return movieArrayList;
    }

    public void insert(Context context){
        ContentValues values = getContent();
        Uri uri = MovieProvider.CONTENT_URI;
        context.getContentResolver().insert(uri,
                values);
    }

    public void update(Context context){
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_NAME_FAVORITE,this.isFavorite()?1:0);

        context.getContentResolver().update(MovieProvider.CONTENT_URI,
                values,
                MovieEntry.COLUMN_NAME_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
