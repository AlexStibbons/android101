package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.proto_korzo.R;
import com.example.proto_korzo.adapters.ViewPageAdapter;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.MovieDBAPI;
import com.example.proto_korzo.database.model.Movie;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

// 1. check network connectivity
// 2. get user data (using the passed on user id) from DB
// 3. populate faves list with user's faves
// 4. populate all list with random films
// 5. persist when clicked fave/unfave
// 6. start new activity when clicked on movie

public class ListsActivity extends AppCompatActivity {

    private static final String TAG = "ListsActivity";

    private DBUserMovie database;
    private MovieDBAPI api;

    TextView textId; // for testing

    //    **************** RECYCLER VIEW ****************
    // declare everything that needs to be used
    // in this case, that would be all the lists used in adapter
    private List<Movie> dummyMovies = new ArrayList<>();
    List<Movie> userFaves  = new ArrayList<>();
    // the list needs to be one list of movies
    // retrieved from moviedb api
    // or from userId - favourite movies

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lists);

        Intent i = getIntent();
        Long id = i.getLongExtra(LoginActivity.EXTRA_ID, -1);
        Log.e(TAG, "ACTIVITY GOTTEN ID: " + id);

        // VIEW PAGER & TABLAYOUT FOR TABS + FRAGMENTS
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), id));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //database = DBUserMovie.getInstance(this);
        //api = MovieDBAPI.getInstance();

        //btnFavourite = (ToggleButton) findViewById(R.id.btn_favorite);



        // no communication with DB from main!
        //User user = database.getUserDao().getUserById(id);
        //userFaves = database.getUserMovieDao().getMoviesByUserId(id);

       //textId = (TextView) findViewById(R.id.idView);
       //textId.setText("This user's id is: " + id + " \nEmail: " + user.getEmail());

        // populate recycler view
       // initDummyMovies();

        //
    }
/*
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");
        // find the recycler view
        // this is the id in xml where recycler appears
        RecyclerView recyclerView = findViewById(R.id.movie_list_recycler_view);
        // get the adapter too
        RecyclerViewAdapterAllMovies adapter = new RecyclerViewAdapterAllMovies(dummyMovies, userFaves, this);
        // set the adapter to the recycler
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initDummyMovies() {
        Log.d(TAG, "initImageBitmaps: prepping bitmaps");

        String img1 = "https://i2.wp.com/www.tor.com/wp-content/uploads/2019/09/vetting-final.jpg?fit=740%2C1067&type=vertical&quality=100&ssl=1";
        String img2 = "https://i0.wp.com/www.tor.com/wp-content/uploads/2019/07/forhecancreep_full.jpg?fit=740%2C777&type=vertical&quality=100&ssl=1";
        String img3 ="https://i1.wp.com/www.tor.com/wp-content/uploads/2019/09/Hundrethhouse_-full.jpeg?fit=740%2C958&type=vertical&quality=100&ssl=1";
        String img4 = "http://strangehorizons.com/wordpress/wp-content/uploads/2019/07/regret-return_600px.png";
        String img5 = "http://strangehorizons.com/wordpress/wp-content/uploads/2019/08/FullSomedayWeWill-402x500.png";

        List<Movie> input = Arrays.asList(
                new Movie("Title 1 ", "desc 1", img1),
                new Movie("Title 2", "desc 2", img2),
                new Movie("Title 3", "desc 3", img3),
                new Movie("Title 4", "desc 4", img4),
                new Movie("Title 5", "desc 5", img5),
                new Movie("Title 6", "desc 6", img1),
                new Movie("Title 7", "desc 7", img2),
                new Movie("Title 8", "desc 8", img3),
                new Movie("Title 9", "desc 9", img4),
                new Movie("Title 10", "desc 10", img5),
                new Movie("Title 11", "desc 11", img1),
                new Movie("Title 12", "desc 12", img2),
                new Movie("Title 13", "desc 13", img3),
                new Movie("Title 14", "desc 14", img4),
                new Movie("Title 15", "desc 15", img5));

        dummyMovies.addAll(input);
        //userFaves.add(input.get(0));
        initRecyclerView();
    }*/
}
