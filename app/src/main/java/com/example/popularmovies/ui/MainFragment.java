package com.example.popularmovies.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.DBOperator;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.util.HttpUtil;
import com.example.popularmovies.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private static final String TAG = MainFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private String mSort;
    private List<Movie> mMovies = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mSort = SPUtils.getSort(getActivity());
        if(savedInstanceState == null){
        loadData(mSort);
        }else {
            mMovies = (ArrayList)savedInstanceState.getSerializable("data");
            showData(mMovies);
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        String currSort = SPUtils.getSort(getActivity());
        if(!mSort.equals(currSort)){
            mSort = currSort;
            loadData(mSort);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data",(ArrayList)mMovies);
    }

    /**
     * load data , may come from remote or local database
     * @param sort
     */
    private void loadData(String sort){
        String[] sorts = getResources().getStringArray(R.array.pref_sort_entries_value);
        if(sort.equals(sorts[2])){
            mMovies = DBOperator.getInstance(getContext()).getFavorites();
            showData(mMovies);
        }else {

            loadRemoteData();
        }
    }

    /**
     * popular,top_rated from remote
     */
    protected void loadRemoteData() {

        new Thread(){
            @Override
            public void run() {
                try {

                    String urlStr = Constants.API_BASE_URL + "movie/" + mSort + "?api_key=" + Constants.API_KEY ;
                    String jsonStr = HttpUtil.getJsonStr(urlStr);
                    if(jsonStr != null){


                        JSONObject root = new JSONObject(jsonStr);
                        JSONArray json_result = root.getJSONArray("results");
                        mMovies.clear();
                        for (int i = 0; i < json_result.length(); i++) {
                            JSONObject movieJson = json_result.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.poster_path = movieJson.getString("poster_path");
                            movie.adult = movieJson.getBoolean("adult");
                            movie.overview = movieJson.getString("overview");
                            movie.release_date = movieJson.getString("release_date");
                            movie.original_title = movieJson.getString("original_title");
                            movie.id = movieJson.getInt("id");
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
                            mMovies.add(movie);
                        }
                        if(getActivity() == null) return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showData(mMovies);

                            }
                        });
                    }

                }  catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void showData(List<Movie> movies) {
        MovieAdapter adapter = new MovieAdapter(getActivity(),movies);
        adapter.setOnItemClickListener((MainActivity)getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }
}
