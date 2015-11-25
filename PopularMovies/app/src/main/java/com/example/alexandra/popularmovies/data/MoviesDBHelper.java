package com.example.alexandra.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.alexandra.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by Alexandra on 23-11-2015.
 */
public class MoviesDBHelper
        extends SQLiteOpenHelper {

    private static MoviesDBHelper singleton;
    private static final int DATA_BASE_VERSION = 1;
    private static final String DATA_BASE_NAME = "movies.db";

    public static MoviesDBHelper getInstance(final Context context) {
        if (singleton == null) {
            singleton = new MoviesDBHelper(context);
        }
        return singleton;
    }

    public MoviesDBHelper(Context context){
        super(context,DATA_BASE_NAME,null,DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String COMMA_SEP = ",";
        final String SQL_CREATE_MOVIE_TABLE = " CREATE TABLE " +
                MovieEntry.TABLE_NAME +
                "(" +
                MovieEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                MovieEntry.COLUMN_NAME_TITLE + " TEXT " + COMMA_SEP +
                MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT " + COMMA_SEP +
                MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT " + COMMA_SEP +
                MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT " + COMMA_SEP +
                MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL " + COMMA_SEP +
                MovieEntry.COLUMN_NAME_POPULARITY + " REAL " + COMMA_SEP +
                MovieEntry.COLUMN_NAME_FAVORITE + " BOOLEAN " +
                ")";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
