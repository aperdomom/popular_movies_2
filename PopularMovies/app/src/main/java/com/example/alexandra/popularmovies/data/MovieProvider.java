package com.example.alexandra.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.alexandra.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by Alexandra on 24-11-2015.
 */
public class MovieProvider extends ContentProvider {
    private MoviesDBHelper mDB;
    private SQLiteDatabase db;
    private static final String AUTHORITY = "com.example.alexandra.popularmovies.data.MovieProvider";
    public static final  int    MOVIES    = 100;
    public static final  int    MOVIE_ID  = 110;
    private static final String MOVIES_BASE_PATH = "movies";
    public static final  Uri    CONTENT_URI      = Uri.parse("content://" + AUTHORITY
            + "/" + MOVIES_BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY,
                MOVIES_BASE_PATH,
                MOVIES);
        sURIMatcher.addURI(AUTHORITY,
                MOVIES_BASE_PATH + "/#",
                MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        mDB = new MoviesDBHelper(getContext());
        db = mDB.getWritableDatabase();

        if (db == null) {
            return false;
        }

        if (db.isReadOnly()) {
            db.close();
            db = null;
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MOVIE_ID:
                queryBuilder.appendWhere(MovieEntry.COLUMN_NAME_ID + "="
                        + uri.getLastPathSegment());
                break;
            case MOVIES:
                queryBuilder.appendWhere(MovieEntry.COLUMN_NAME_FAVORITE + " = 1 ");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        return queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      ContentValues values) {
        if (sURIMatcher.match(uri) != MOVIES) {
            throw new IllegalArgumentException("Invalid URI: "+uri);
        }

        try {
            long id = db.insert(MovieEntry.TABLE_NAME,null,values);
            if (id > 0) {
                return ContentUris.withAppendedId(uri,
                        id);
            }
        }catch (Exception ex){
            Log.e("MovieProvider","Error inserting into table: " + MovieEntry.TABLE_NAME);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        int count;
        switch (sURIMatcher.match(uri)){
            case MOVIES:
                count = db.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case MOVIE_ID:
                count = db.update(MovieEntry.TABLE_NAME, values, MovieEntry.COLUMN_NAME_ID + " = " +
                        uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""),
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        return count;
    }
}
