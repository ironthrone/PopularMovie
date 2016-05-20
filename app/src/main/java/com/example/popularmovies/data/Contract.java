package com.example.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/5/20.
 */
public class Contract {



    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";
         public static final String TITLE = "title";
         public static final String RELEASE_DATE = "release_date";
         public static final String POSTER_PATH = "poster_path";
         public static final String VOTE_AVERAGE = "vote_average";
         public static final String OVERVIEW = "overview";
         public static final String FAVORITE = "favorite";

        public static final String CREATE_TABLE_MOVIE = "create table " + TABLE_NAME + "("
                + _ID +" integer primary key autoincrement,"
                + TITLE + " text not null,"
                + RELEASE_DATE + " text not null,"
                + POSTER_PATH + " text not null,"
                + VOTE_AVERAGE + " real not null,"
                + OVERVIEW + " text not null,"
                + FAVORITE + " integer not null"
                + ");";
    }
}
