package com.example.popularmovies.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

/**
 *
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }

    private TextView mTitleTV;
    private TextView mResDateTV;
    private TextView mRatingTV;
    private TextView mSynopsisTV;
    private ImageView mPosterIV;

    private Movie mMovie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);
        mTitleTV = (TextView)rootView.findViewById(R.id.title);
        mResDateTV = (TextView)rootView.findViewById(R.id.release_date);
        mRatingTV = (TextView)rootView.findViewById(R.id.rating);
        mSynopsisTV = (TextView)rootView.findViewById(R.id.synopsis);
        mPosterIV = (ImageView) rootView.findViewById(R.id.poster);
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
//        new TrailerTask().execute(null);
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


    private class TrailerTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }




}
