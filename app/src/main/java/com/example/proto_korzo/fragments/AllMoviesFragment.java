package com.example.proto_korzo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proto_korzo.R;
import com.example.proto_korzo.adapters.RecyclerViewAdapterAllMovies;
import com.example.proto_korzo.database.model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 1. get userId from ListsActivity
// 2. get movies from external API [mock list atm]
// 3. check which ones are favourite movies
// 4. show list in recycler view
// 4a. movies which are among favourites have a full favourite icon

public class AllMoviesFragment extends Fragment {

    private static final String TAG = "AllMoviesFragment";

    private final long id;

    private List<Movie> dummyMovies = new ArrayList<>();
    List<Movie> userFaves  = new ArrayList<>();

    RecyclerView recyclerView;
    private RecyclerViewAdapterAllMovies adapter;

    public AllMoviesFragment(long id){
        super();
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        Log.e(TAG, "MOVIES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        initDummyMovies();

        return rootView;
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
        userFaves.add(input.get(0));
        initRecyclerView();
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        adapter = new RecyclerViewAdapterAllMovies(dummyMovies, userFaves, this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    private void displayList(List<Movie> movies){
        adapter.setList(movies);
    }



}
