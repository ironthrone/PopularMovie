package com.example.popularmovies.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/10.
 */
public class Movie implements Serializable{
    public String poster_path;
    public boolean adult;
    public String overview;
            public String release_date;
            public ArrayList<Integer> genre_ids;
            public int id;
            public String original_title;
            public String original_language;
            public String title;
            public String backdrop_path;
            public double popularity;
            public int vote_count;
            public boolean video;
            public double vote_average;
    public boolean favorite;
}
