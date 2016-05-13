package com.example.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static Activity mContext;
    private ArrayList<Movie> mMovies;
    public MovieAdapter(Activity context, ArrayList<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, R.layout.item_movie,null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(Constants.BASE_POSTER_URL  + mMovies.get(position).poster_path)
                .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("data",mMovies.get(position));
                    mContext.startActivity(intent);
                }
            });
    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

    }
}
