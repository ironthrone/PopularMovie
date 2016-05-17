package com.example.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/5/16.
 */
public class NetworkUtil {
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NO_CONNECT = 0;

    public static int getConnectivityStatus(Context context){
        int status = TYPE_NO_CONNECT;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null){
            if(ni.getType() == ConnectivityManager.TYPE_WIFI) status = TYPE_WIFI;
            if(ni.getType() == ConnectivityManager.TYPE_MOBILE) status = TYPE_MOBILE;
        }
        return status;
    }

    public static String getConnectivityStatusString(Context context){
        int status = getConnectivityStatus(context);
        String desc = null;
        switch (status){
            case TYPE_WIFI:
                desc = "Wifi enabled";
                break;
            case TYPE_MOBILE:
                desc = "Mobile enabled";
                break;
            case TYPE_NO_CONNECT:
                desc = "Not connected to Internet";
                break;
        }
        return desc;
    }
}
