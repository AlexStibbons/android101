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
import com.example.proto_korzo.adapters.RecyclerViewAdapterFaveMovies;
import com.example.proto_korzo.database.model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaveMoviesFragment extends Fragment {

    private static final String TAG = "FaveMoviesFragment";

    private long id;
    private List<Movie> userFaves = new ArrayList<>();
    RecyclerView recyclerView;

    public FaveMoviesFragment(long id) {
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        Log.e(TAG, "FAVES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        initDummyMovies();

        return rootView;
    }

    private void initRecyclerView() {

        Log.d(TAG, "initRecyclerView: started");

        RecyclerViewAdapterFaveMovies adapter = new RecyclerViewAdapterFaveMovies(userFaves, this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));



    }


    private void initDummyMovies() {


        String img1 = "https://i2.wp.com/www.tor.com/wp-content/uploads/2019/09/vetting-final.jpg?fit=740%2C1067&type=vertical&quality=100&ssl=1";
        String img2 = "https://i0.wp.com/www.tor.com/wp-content/uploads/2019/07/forhecancreep_full.jpg?fit=740%2C777&type=vertical&quality=100&ssl=1";
        String img3 ="https://i1.wp.com/www.tor.com/wp-content/uploads/2019/09/Hundrethhouse_-full.jpeg?fit=740%2C958&type=vertical&quality=100&ssl=1";
        String img4 = "http://strangehorizons.com/wordpress/wp-content/uploads/2019/07/regret-return_600px.png";
        String img5 = "http://strangehorizons.com/wordpress/wp-content/uploads/2019/08/FullSomedayWeWill-402x500.png";

        List<Movie> input = Arrays.asList(
                new Movie("Title 1 ", "desc 1", img1),
                new Movie("Title 7", "desc 7", img2),
                new Movie("Title 8", "desc 8", img3),
                new Movie("Title 12", "desc 12", img2),
                new Movie("Title 13", "desc 13", img3),
                new Movie("Title 14", "desc 14", img4),
                new Movie("Title 15", "desc 15", img5));

        userFaves.addAll(input);

        initRecyclerView();
    }


}
