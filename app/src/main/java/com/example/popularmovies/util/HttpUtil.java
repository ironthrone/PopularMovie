package com.example.popularmovies.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/20.
 */
public class HttpUtil {
    public static String getJsonStr(String urlStr){
        String jsonStr = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode != 200) return jsonStr;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            jsonStr = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonStr;
    }
}
