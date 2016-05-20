package com.example.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/20.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";
    private static final int VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.MovieEntry.CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Contract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
