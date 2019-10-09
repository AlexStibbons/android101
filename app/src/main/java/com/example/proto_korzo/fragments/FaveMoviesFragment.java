package com.example.proto_korzo.fragments;

import android.os.AsyncTask;
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
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FaveMoviesFragment extends Fragment {

    private static final String TAG = "FaveMoviesFragment";

    // *** interface ***
    public interface FetchFaves {
        void onFavesFetched(List<Movie> userFaves);
    }

    private DBUserMovie database;
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

        database = DBUserMovie.getInstance(getActivity());

        Log.e(TAG, "FAVES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        //initDummyMovies();
        fetchFaves();

        initRecyclerView();

        return rootView;
    }


    FetchFaves favesInterface = new FetchFaves() {

        @Override
        public void onFavesFetched(List<Movie> faves) {
            userFaves.clear();
            userFaves.addAll(faves);
            initRecyclerView();
        }
    };

    public void fetchFaves() {
        // start Async task
        FetchFavesTask fetchFavesTask = new FetchFavesTask(favesInterface, database);
        fetchFavesTask.execute(id);

    }

    private void initRecyclerView() {

        Log.d(TAG, "initRecyclerView: started");

        RecyclerViewAdapterFaveMovies adapter = new RecyclerViewAdapterFaveMovies(userFaves, this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


    }

    private class FetchFavesTask extends AsyncTask<Long, Void, List<Movie>> {

        private FetchFaves favesInterface;
        private DBUserMovie database;

        public FetchFavesTask(FetchFaves favesInterface, DBUserMovie database) {
            this.favesInterface = favesInterface;
            this.database = database;
        }

        @Override
        protected List<Movie> doInBackground(Long... longs) {

            List<Movie> faves = database.getUserMovieDao().getMoviesByUserId(longs[0]);

            return faves;
        }

        @Override
        protected void onPostExecute(List<Movie> faves) {
            super.onPostExecute(faves);

            this.favesInterface.onFavesFetched(faves);
        }
    }

}
