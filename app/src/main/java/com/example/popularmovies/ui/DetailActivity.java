package com.example.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

        private TextView mTitleTV;
    private TextView mResDateTV;
    private TextView mRatingTV;
    private TextView mSynopsisTV;
    private ImageView mPosterIV;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovie = (Movie)getIntent().getSerializableExtra("data");

        mTitleTV = (TextView)findViewById(R.id.title);
        mResDateTV = (TextView)findViewById(R.id.release_date);
        mRatingTV = (TextView)findViewById(R.id.rating);
        mSynopsisTV = (TextView)findViewById(R.id.synopsis);
        mPosterIV = (ImageView) findViewById(R.id.poster);
        showData();
    }

    private void showData() {
        mTitleTV.setText(mMovie.original_title);
        mResDateTV.setText(mMovie.release_date);
        mRatingTV.setText(mMovie.vote_average + "/10");
        mSynopsisTV.setText(mMovie.overview);
        Picasso.with(this)
                .load(Constants.BASE_POSTER_URL + mMovie.poster_path)
                .into(mPosterIV);
    }
}
