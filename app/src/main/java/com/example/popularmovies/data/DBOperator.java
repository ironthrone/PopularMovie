package com.example.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/20.
 */
public class DBOperator {
    private static SQLiteDatabase mDB;
    private static DBOperator mOperator;
    private DBOperator(Context context){
        mDB = new DBOpenHelper(context).getWritableDatabase();

    }
    public static DBOperator getInstance(Context context){
        if(mOperator == null){
            mOperator = new DBOperator(context);
        }
        return mOperator;
    }

    public void insert(Movie movie){
        ContentValues values = new ContentValues();
        values.put(Contract.MovieEntry._ID,movie.id);
        values.put(Contract.MovieEntry.TITLE,movie.title);
        values.put(Contract.MovieEntry.OVERVIEW,movie.overview);
        values.put(Contract.MovieEntry.RELEASE_DATE,movie.release_date);
        values.put(Contract.MovieEntry.POSTER_PATH,movie.poster_path);
        values.put(Contract.MovieEntry.VOTE_AVERAGE,movie.vote_average);
        values.put(Contract.MovieEntry.FAVORITE,movie.favorite);
        mDB.insert(Contract.MovieEntry.TABLE_NAME,null,values);
    }

    public boolean isFavorite(int id){
        Cursor cursor = mDB.query(Contract.MovieEntry.TABLE_NAME,
                new String[]{Contract.MovieEntry.FAVORITE},
                Contract.MovieEntry._ID + " = ?",
                new String[]{id + ""},
                null,
                null,
                null);
        if(cursor.moveToFirst()){

            int result = cursor.getInt(cursor.getColumnIndex(Contract.MovieEntry.FAVORITE));
        cursor.close();
        return result == 1;
        }else {
            return false;
        }
    }

    public List<Movie> getFavorites(){
        List<Movie> favorites = new ArrayList<>();
        Cursor cursor = mDB.query(Contract.MovieEntry.TABLE_NAME,
                null,
                Contract.MovieEntry.FAVORITE + " = ?",
                new String[]{1 + ""},
                null,
                null,
                null);
        if(cursor.moveToNext()){
            Movie movie = new Movie();
            movie.id = cursor.getInt(cursor.getColumnIndex(Contract.MovieEntry._ID));
            movie.title = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.TITLE));
            movie.release_date = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.RELEASE_DATE));
            movie.overview = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.OVERVIEW));
            movie.poster_path = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.POSTER_PATH));
            movie.favorite = cursor.getInt(cursor.getColumnIndex(Contract.MovieEntry.FAVORITE)) == 1;
            movie.vote_average = cursor.getDouble(cursor.getColumnIndex(Contract.MovieEntry.VOTE_AVERAGE));
            favorites.add(movie);
        }
        return favorites;
    }
}
