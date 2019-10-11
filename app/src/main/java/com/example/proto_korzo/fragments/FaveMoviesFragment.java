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
import com.example.proto_korzo.asyncTasks.AsyncTaskManager;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FaveMoviesFragment extends Fragment {

    private static final String TAG = "FaveMoviesFragment";

    private DBUserMovie database;
    private long id;
    private List<Movie> userFaves = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapterFaveMovies adapter;

    public FaveMoviesFragment(long id) {
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        database = DBUserMovie.getInstance(getActivity());

        Log.e(TAG, "FAVES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

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
        public void onFave(long movieId) {
            // this has no functionality here
            // all of them are favourites
            // they can only be unfaved
        }

        @Override
        public void onUnfave(long movieId) {
            AsyncTaskManager.removeFaveMovie(database, id, movieId, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    adapter.setFaveMovies(movies);
                }
            });
        }
    };

    // NO
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
