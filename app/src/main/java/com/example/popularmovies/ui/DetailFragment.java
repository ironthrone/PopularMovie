package com.example.popularmovies.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.data.Review;
import com.example.popularmovies.data.Trailer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DetailFragment extends Fragment {


    private LinearLayout mReviewContainer;
    private LinearLayout mTrailerContainer;

    public DetailFragment() {
        // Required empty public constructor
    }

    private TextView mTitleTV;
    private TextView mResDateTV;
    private TextView mRatingTV;
    private TextView mSynopsisTV;
    private ImageView mPosterIV;

    private Movie mMovie;

    private List<Trailer> mTrailers = new ArrayList<>();
    private List<Review> mReviews = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);
        mTitleTV = (TextView)rootView.findViewById(R.id.title);
        mResDateTV = (TextView)rootView.findViewById(R.id.release_date);
        mRatingTV = (TextView)rootView.findViewById(R.id.rating);
        mSynopsisTV = (TextView)rootView.findViewById(R.id.synopsis);
        mPosterIV = (ImageView) rootView.findViewById(R.id.poster);
        mTrailerContainer = (LinearLayout)rootView.findViewById(R.id.trailer_container);
        mReviewContainer = (LinearLayout)rootView.findViewById(R.id.review_container);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovie = (Movie) getArguments().getSerializable("data");
        showData();
        getTrailer();
        getReviews();
    }

    private void getReviews() {
    }

    private void getTrailer() {
        new TrailerTask().execute(mMovie.id);
    }

    private void showData() {
        mTitleTV.setText(mMovie.original_title);
        mResDateTV.setText(mMovie.release_date);
        mRatingTV.setText(mMovie.vote_average + "/10");
        mSynopsisTV.setText(mMovie.overview);
        Picasso.with(getActivity())
                .load(Constants.BASE_POSTER_URL + mMovie.poster_path)
                .into(mPosterIV);
    }


    private class TrailerTask extends AsyncTask<Integer,Void,List<Trailer>> {


        @Override
        protected List<Trailer> doInBackground(Integer... params) {
            try {
                URL url = new URL(Constants.API_BASE_URL + "movie/"
                + mMovie.id + "/videos?api_key=" + Constants.API_KEY);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                int code = connection.getResponseCode();
                if(code != 200){
                    return mTrailers;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = br.readLine()) != null){
                sb.append(str);
                }
                String jsonStr = sb.toString();

                JSONObject rootJson = new JSONObject(jsonStr);
                JSONArray json_result = rootJson.getJSONArray("results");
                for (int i = 0; i < json_result.length(); i++) {
                    Trailer trailer = new Trailer();
                    JSONObject json_trailer = json_result.getJSONObject(i);
                    trailer.id = json_trailer.getString("id");
                    trailer.iso_639_1 = json_trailer.getString("iso_639_1");
                    trailer.key = json_trailer.getString("key");
                    trailer.name = json_trailer.getString("name");
                    trailer.site = json_trailer.getString("site");
                    mTrailers.add(trailer);
                }
            }catch (Exception e){

            }
            return mTrailers;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);
            for(Trailer trailer:trailers){
                addTrailerView(trailer);
            }
        }
    }

    private void addTrailerView(Trailer trailer){
        View trailerView = getActivity().getLayoutInflater().inflate(R.layout.item_trailer,null);
        ((TextView)trailerView.findViewById(R.id.trailer_name)).setText(trailer.name);
        mTrailerContainer.addView(trailerView);
    }


}
