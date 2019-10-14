package com.example.proto_korzo.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.proto_korzo.Utils;
import com.example.proto_korzo.activities.MovieActivity;
import com.example.proto_korzo.adapters.RecyclerViewAdapterAllMovies;
import com.example.proto_korzo.asyncTasks.AsyncTaskManager;
import com.example.proto_korzo.broadcastReceivers.FaveChangeBroadcast;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class AllMoviesFragment extends Fragment {

    private static final String TAG = "AllMoviesFragment";

    private final long id; // userId

    private List<Movie> dummyMovies = new ArrayList<>();
    List<Movie> userFavesMovies = new ArrayList<>();
    List<Long> userFavesIds = new ArrayList<>();
    private DBUserMovie database;

    RecyclerView recyclerView;
    private RecyclerViewAdapterAllMovies adapter;
    FaveChangeBroadcast faveReceiver;
    IntentFilter intentFilter;

    public AllMoviesFragment(long id) {
        super();
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        database = DBUserMovie.getInstance(getActivity());

        // register broadcast receiver
        intentFilter = new IntentFilter(Utils.IF_FAVE_CHANGED);
        //faveReceiver = new FaveChangeBroadcast();
        getContext().registerReceiver(br, intentFilter);

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        Log.e(TAG, "MOVIES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        // 1. fetch all movies - AsyncTask 1
        // 2. fetch user favourites - Async Task 2
        // 3. initRecyclerView()
        fetchMovies();

        return rootView;
    }

    private void fetchMovies() {

        AsyncTaskManager.fetchAllMovies(database, new AsyncTaskManager.TaskListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                dummyMovies.clear();
                dummyMovies.addAll(movies);

                // now start another async task
                AsyncTaskManager.fetchFaveMovies(database, id, new AsyncTaskManager.TaskListener() {
                    @Override
                    public void onMoviesFetched(List<Movie> movies) {
                        userFavesMovies.clear();
                        userFavesMovies.addAll(movies);
                        initRecyclerView();
                    }
                });

            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        adapter = new RecyclerViewAdapterAllMovies(dummyMovies, userFavesMovies,
                this.getActivity(), faveClickListener); // the fragment itself "is" an interface
        // because it inherits the DefaultFragment
        // which implements the interface, or if it
        // implements the interface
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }


    Listeners.OnFaveClick faveClickListener = new Listeners.OnFaveClick() {
        @Override
        public void onFave(long movieId) {
            AsyncTaskManager.setFaveMovie(database, id, movieId, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    //userFavesMovies.clear();
                    //userFavesMovies.addAll(movies);

                    // why not change list in fragment [userFaveMovies], then pass that list?
                    // when that one is passed to adapter, view does not work properly
                    adapter.setFaveMovies(movies);

                    // also, the entire list should not be changed
                    // just the one movie
                }
            });
            Log.e(TAG, "onFave: sending BR");
            getContext().sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));
            Log.e(TAG, "onFave: SENT BR");
        }

        @Override
        public void onUnfave(long movieId) {
            AsyncTaskManager.removeFaveMovie(database, id, movieId, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    // userFavesMovies.clear();
                    //userFavesMovies.addAll(movies);
                    adapter.setFaveMovies(movies);
                }
            });
            // send BR here
            Log.e(TAG, "onUNFave: sending BR");
            getContext().sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));
            Log.e(TAG, "onUNFave: SENT BR");
        }

        @Override
        public void onMovieItemClick(long movieId, boolean isFave) {

            Intent intent = new Intent(getContext(), MovieActivity.class);
            // pass value with intent
            // movie object + fave boolean
            intent.putExtra("MovieIdExtra", movieId);
            intent.putExtra("IsFaveExtra", isFave);
            intent.putExtra("userIdExtra", id);
            // start new activity via intent, obvs
            startActivity(intent);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        getContext().unregisterReceiver(br);
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utils.IF_FAVE_CHANGED.equals(intent.getAction())) {
                fetchMovies();
                // because it receives it's own change
                // it refreshes and pushed screen back to top
                // that is, moves screen position all the way up
                // as though it was created again [which it was]
            }

        }
    };
}
