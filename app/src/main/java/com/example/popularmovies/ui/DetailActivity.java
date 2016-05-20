package com.example.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Movie movie = (Movie)getIntent().getSerializableExtra("data");

        if(savedInstanceState == null){

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("data",movie);
        fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container_detail,
                    fragment).commit();
        }
    }



}
