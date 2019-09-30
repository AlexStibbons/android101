package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proto_korzo.R;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListsActivity extends AppCompatActivity {
    private static final String TAG = "ListsActivity";
    private DBUserMovie database;
    TextView textId;

    //    **************** RECYCLER VIEW ****************
    // declare everything that needs to be used
    // in this case, that would be all the lists used in adapter
    private List<String> mTitles = Arrays.asList("Title 1", "Title 2", "Title 3", "Title 4", "Title 5",
            "Title 6", "Title 7", "Title 8", "Title 9", "Title 10",
            "Title 11", "Title 12", "Title 13", "Title 14", "Title 15");
    private List<String> mImages = new ArrayList<>();
    // the list needs to be one list of movies
    // retreived from moviedb api
    // or from userId - favourite movies


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_list);

        database = DBUserMovie.getInstance(this);

        Intent i = getIntent();
        Long id = i.getLongExtra(LoginActivity.EXTRA_ID, -1);

        // no communication with DB from main!
        User user = database.getUserDao().getUserById(id);

        textId = (TextView) findViewById(R.id.idView);
        textId.setText("This user's id is: " + id + " \nEmail: " + user.getEmail());

        // populate recycler view
        initImageBitmaps();
        // check network connectivity
        // get user data (using the passed on user id) from DB
        // populate faves list with user's faves
        // populate all list with random films
        //  add a star to corner of cards
        //  persist when clicked fave/unfave
        //  on list click (AdapterView / OnItemClickListener) open new activity
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        // find the recycler view
        // this is the id in xml where recycler appears
        RecyclerView recyclerView = findViewById(R.id.movie_list_recycler_view);

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
        mImages.add("https://i2.wp.com/www.tor.com/wp-content/uploads/2019/09/vetting-final.jpg?fit=740%2C1067&type=vertical&quality=100&ssl=1");
        mImages.add("https://i0.wp.com/www.tor.com/wp-content/uploads/2019/07/forhecancreep_full.jpg?fit=740%2C777&type=vertical&quality=100&ssl=1");
        mImages.add("https://i1.wp.com/www.tor.com/wp-content/uploads/2019/09/Hundrethhouse_-full.jpeg?fit=740%2C958&type=vertical&quality=100&ssl=1");
        mImages.add("http://strangehorizons.com/wordpress/wp-content/uploads/2019/07/regret-return_600px.png");
        mImages.add("http://strangehorizons.com/wordpress/wp-content/uploads/2019/08/FullSomedayWeWill-402x500.png");
        mImages.add("https://i2.wp.com/www.tor.com/wp-content/uploads/2019/09/vetting-final.jpg?fit=740%2C1067&type=vertical&quality=100&ssl=1");
        mImages.add("https://i0.wp.com/www.tor.com/wp-content/uploads/2019/07/forhecancreep_full.jpg?fit=740%2C777&type=vertical&quality=100&ssl=1");
        mImages.add("https://i1.wp.com/www.tor.com/wp-content/uploads/2019/09/Hundrethhouse_-full.jpeg?fit=740%2C958&type=vertical&quality=100&ssl=1");
        mImages.add("http://strangehorizons.com/wordpress/wp-content/uploads/2019/07/regret-return_600px.png");
        mImages.add("http://strangehorizons.com/wordpress/wp-content/uploads/2019/08/FullSomedayWeWill-402x500.png");

        initRecyclerView();
    }

}
