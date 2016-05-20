package com.example.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.DBOperator;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.data.Review;
import com.example.popularmovies.data.Trailer;
import com.example.popularmovies.util.HttpUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private Button mFavoriteBT;

    private Movie mMovie;

    private List<Trailer> mTrailers = new ArrayList<>();
    private List<Review> mReviews = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);
        mTitleTV = (TextView)rootView.findViewById(R.id.title);
        mResDateTV = (TextView)rootView.findViewById(R.id.release_date);
        mRatingTV = (TextView)rootView.findViewById(R.id.rating);
        mSynopsisTV = (TextView)rootView.findViewById(R.id.synopsis);
        mPosterIV = (ImageView) rootView.findViewById(R.id.poster);
        mFavoriteBT = (Button) rootView.findViewById(R.id.favorite);

        mFavoriteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovie.favorite = !mMovie.favorite;
                DBOperator.getInstance(getActivity()).insert(mMovie);
        refreshFavoriteBT(mMovie.favorite);
            }
        });
        mTrailerContainer = (LinearLayout)rootView.findViewById(R.id.trailer_container);
        mReviewContainer = (LinearLayout)rootView.findViewById(R.id.review_container);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovie = (Movie) getArguments().getSerializable("data");
        mMovie.favorite = DBOperator.getInstance(getActivity()).isFavorite(mMovie.id);
        showData();
        getTrailer();
        getReviews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_fragment,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share){
            if(mTrailers.size() > 0) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v=" + mTrailers.get(0).key);
            intent.setType("text/plain");
            startActivity(intent);
            }else {
                Toast.makeText(getActivity(), "Nothing to share", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getReviews() {
        new ReviewTask().execute(mMovie.id);
    }

    private void getTrailer() {
        new TrailerTask().execute(mMovie.id);
    }

    private void showData() {
        mTitleTV.setText(mMovie.title);
        mResDateTV.setText(mMovie.release_date);
        mRatingTV.setText(mMovie.vote_average + "/10");
        mSynopsisTV.setText(mMovie.overview);
        Picasso.with(getActivity())
                .load(Constants.BASE_POSTER_URL + mMovie.poster_path)
                .into(mPosterIV);
        refreshFavoriteBT(mMovie.favorite);
    }

    private void refreshFavoriteBT(boolean favorite){
        if(favorite){
            mFavoriteBT.setText("Remove Favorite");
        }else{
            mFavoriteBT.setText("Mark As\nFavorite");

        }

    }

    private void addTrailerView(final Trailer trailer){
        if(getActivity() == null) return;

        View trailerView = getActivity().getLayoutInflater().inflate(R.layout.item_trailer,null);
        ((TextView)trailerView.findViewById(R.id.trailer_name)).setText(trailer.name);
        trailerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailer.key));
                startActivity(intent);
            }
        });
        mTrailerContainer.addView(trailerView);
    }

    private void addReviewView(Review review){
        if(getActivity() == null) return;
        View reviewView = getActivity().getLayoutInflater().inflate(R.layout.item_review,null);
        ((TextView)reviewView.findViewById(R.id.review_content)).setText(review.content);
        ((TextView)reviewView.findViewById(R.id.review_author)).setText("Author: " + review.author);
        mReviewContainer.addView(reviewView);
    }

    private class ReviewTask extends AsyncTask<Integer,Void,List<Review>>{

        @Override
        protected List<Review> doInBackground(Integer... params) {
                try{
                String urlStr = Constants.API_BASE_URL + "movie/" + params[0] + "/reviews?api_key="
                + Constants.API_KEY;
            String jsonStr = HttpUtil.getJsonStr(urlStr);
            if(jsonStr != null){

            JSONObject rootJson = new JSONObject(jsonStr);
            JSONArray json_results = rootJson.getJSONArray("results");
                    for (int i = 0; i < json_results.length(); i++) {
                        JSONObject object = json_results.getJSONObject(i);
                        Review review = new Review();
                        review.id = object.getString("id");
                        review.author = object.getString("author");
                        review.content = object.getString("content");
                        review.url = object.getString("url");
                        mReviews.add(review);
                    }
            }
                }catch (Exception e){
                    e.printStackTrace();
                }

            return mReviews;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            for(Review review:reviews){
                addReviewView(review);
            }
        }
    }
    private class TrailerTask extends AsyncTask<Integer,Void,List<Trailer>> {


        @Override
        protected List<Trailer> doInBackground(Integer... params) {
            try {
                String urlStr = Constants.API_BASE_URL + "movie/"
                + mMovie.id + "/videos?api_key=" + Constants.API_KEY;
                String jsonStr = HttpUtil.getJsonStr(urlStr);
                if(jsonStr != null){

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
                }
            }catch (Exception e){
                e.printStackTrace();
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

}
