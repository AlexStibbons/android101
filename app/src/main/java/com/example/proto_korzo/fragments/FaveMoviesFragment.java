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
import com.example.proto_korzo.adapters.RecyclerViewAdapterFaveMovies;
import com.example.proto_korzo.asyncTasks.AsyncTaskManager;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FaveMoviesFragment extends Fragment {

    private static final String TAG = "FaveMoviesFragment";

    private DBUserMovie database;
    private int id;
    private List<Movie> userFaves = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapterFaveMovies adapter;

    public FaveMoviesFragment() {

    }

    public static FaveMoviesFragment getInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("userId", id);

        FaveMoviesFragment faveMoviesFragment = new FaveMoviesFragment();
        faveMoviesFragment.setArguments(args);

        return faveMoviesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        database = DBUserMovie.getInstance(getActivity());
        id = getArguments().getInt("userId");

        // register BR
        IntentFilter intentFilter = new IntentFilter(Utils.IF_FAVE_CHANGED);
        getContext().registerReceiver(br, intentFilter);

        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        // here it recognizes recyclerView, but skips layout bc "there's no adapter attached"
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        fetchFaves();

        return rootView;
    }

    public void fetchFaves() {
        AsyncTaskManager.fetchFaveMovies(database, id, new AsyncTaskManager.TaskListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                userFaves.clear();
                userFaves.addAll(movies);
                initRecyclerView();
            }
        });
    }

    private void initRecyclerView() {

        Log.d(TAG, "initRecyclerView: started");

        adapter = new RecyclerViewAdapterFaveMovies(userFaves,
                this.getActivity(), listener);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    Listeners.OnFaveClick listener = new Listeners.OnFaveClick() {
        @Override
        public void onFave(Movie movie) {
            // this has no functionality here
            // all of them are favourites
            // they can only be unfaved
        }

        @Override
        public void onUnfave(Movie movie) {
            AsyncTaskManager.removeFaveMovie(database, id, movie, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    //adapter.setFaveMovies(movies);
                }
            });

            getContext().sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));
        }

        @Override
        public void onMovieItemClick(int movieId, boolean isFave) {

            Intent intent = new Intent(getContext(), MovieActivity.class);

            intent.putExtra("MovieIdExtra", movieId);
            intent.putExtra("IsFaveExtra", isFave);
            intent.putExtra("userIdExtra", id);

            startActivity(intent);
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(br);
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Utils.IF_FAVE_CHANGED.equals(intent.getAction())) {
                broadcastAction();
            }

        }
    };

    private void broadcastAction() {
        AsyncTaskManager.fetchFaveMovies(database, id, new AsyncTaskManager.TaskListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                adapter.setFaveMovies(movies);
            }
        });
    }


}
