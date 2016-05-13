package com.example.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.data.MoviePage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
    }

    private void loadData() {

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(Constants.BASE_URL + "movie/popular?api_key=" + Constants.API_KEY );
                    HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.connect();
                    int code = connection.getResponseCode();
                    if(code == 404){
                        Toast.makeText(MainActivity.this, "No Resource Founded", Toast.LENGTH_SHORT).show();
                    }
                    if(code == 200){
                             InputStream is = connection.getInputStream();
                        Reader reader = new InputStreamReader(is);

                        StringBuilder strBuilder = new StringBuilder();
                        char[] chars = new char[10];
                        while(reader.read(chars) != -1){
                            strBuilder.append(chars);
                        }
                        String jsonStr = strBuilder.toString();

                        JSONObject object = new JSONObject(jsonStr);
                        final MoviePage page = new MoviePage();
                        page.page = object.getInt("page");
                        page.total_pages = object.getInt("total_pages");
                        page.total_results = object.getInt("total_results");
                        JSONArray jsonArr = object.getJSONArray("results");
                        page.results = new ArrayList<Movie>();
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject movieJson = jsonArr.getJSONObject(i);
                            Movie movie = new Movie();
                                    movie.poster_path = movieJson.getString("poster_path");
                                    movie.adult = movieJson.getBoolean("adult");
                            movie.overview = movieJson.getString("overview");
                            movie.release_date = movieJson.getString("release_date");
                            movie.original_title = movieJson.getString("original_title");
                            movie.original_language = movieJson.getString("original_language");
                            movie.title = movieJson.getString("title");
                            movie.backdrop_path = movieJson.getString("backdrop_path");
                            movie.popularity = movieJson.getDouble("popularity");
                            movie.vote_count = movieJson.getInt("vote_count");
                            movie.video = movieJson.getBoolean("video");
                            movie.vote_average = movieJson.getDouble("vote_average");
                            movie.genre_ids = new ArrayList<>();
                            JSONArray genreArr = movieJson.getJSONArray("genre_ids");
                            for (int j = 0; j < genreArr.length(); j++) {
                                Integer genreJsonObj = genreArr.getInt(j);
                                movie.genre_ids.add(genreJsonObj);
                            }
                            page.results.add(movie);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showData(page);

                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void showData(MoviePage page) {
        MovieAdapter adapter = new MovieAdapter(this,page.results);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
