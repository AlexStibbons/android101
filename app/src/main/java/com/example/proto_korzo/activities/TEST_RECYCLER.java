package com.example.proto_korzo.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proto_korzo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TEST_RECYCLER extends AppCompatActivity {

    private static final String TAG = "TEST_RECYCLER";

    // declare everything that needs to be used
    // in this case, that would be all the lists used in adapter
    private List<String> mTitles = Arrays.asList("Title 1", "Title 2", "Title 3", "Title 4", "Title 5");
    private List<String> mImages = new ArrayList<>();
    // the list needs to be one list of movies
    // retreived from moviedb api
    // or from userId - favourite movies

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_list);

        Log.d(TAG, "onCreate: started");

        initImageBitmaps();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        // find the recycler view
        // this is the id in xml where recycler appears
        RecyclerView recyclerView = findViewById(R.id.movie_list_recycler);

        // get the adapter too
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mImages, mTitles, this);

        // set the adapter to the recycler
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: prepping bitmaps");

        // find some urls
        mImages.add("https://i2.wp.com/www.tor.com/wp-content/uploads/2019/09/vetting-final.jpg?fit=740%2C1067&type=vertical&quality=100&ssl=1");
        mImages.add("https://i0.wp.com/www.tor.com/wp-content/uploads/2019/07/forhecancreep_full.jpg?fit=740%2C777&type=vertical&quality=100&ssl=1");
        mImages.add("https://i1.wp.com/www.tor.com/wp-content/uploads/2019/09/Hundrethhouse_-full.jpeg?fit=740%2C958&type=vertical&quality=100&ssl=1");
        mImages.add("http://strangehorizons.com/wordpress/wp-content/uploads/2019/07/regret-return_600px.png");
        mImages.add("http://strangehorizons.com/wordpress/wp-content/uploads/2019/08/FullSomedayWeWill-402x500.png");

        initRecyclerView();
    }
}
