package com.example.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.popularmovies.R;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SPUtils {

    public static String getSort(Context context){
        SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharePref.getString("sort",context.getString(R.string.pref_default_sort_by));
    }

}
