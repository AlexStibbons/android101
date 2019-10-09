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
import com.example.proto_korzo.adapters.RecyclerViewAdapterAllMovies;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class AllMoviesFragment extends Fragment {

    private static final String TAG = "AllMoviesFragment";

    // *** interface ***
    public interface FetchMovies {
        void onAllMoviesFetched(List<Movie> allMovies);
        void onFaveMoviesFetched(List<Movie> userFaves);
    }

    private final long id;

    private List<Movie> dummyMovies = new ArrayList<>();
    List<Movie> userFavesMovies  = new ArrayList<>();
    List<Long> userFavesIds = new ArrayList<>();
    private DBUserMovie database;

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

        database = DBUserMovie.getInstance(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        Log.e(TAG, "MOVIES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        // 1. fetch all movies - AsyncTask 1
        // 2. fetch user favourites - Async Task 2
        initFetchingMovies();
        // 3. initRecyclerView()
        initRecyclerView();

        return rootView;
    }

    FetchMovies moviesInterface = new FetchMovies() {

        @Override
        public void onAllMoviesFetched(List<Movie> all) {
            dummyMovies.clear();
            dummyMovies.addAll(all);
            FetchFaveMoviesTask fetchFavesTask = new FetchFaveMoviesTask(moviesInterface, database);
            fetchFavesTask.execute(id);
        }

        @Override
        public void onFaveMoviesFetched(List<Movie> faves) {
            userFavesMovies.clear();
            userFavesMovies.addAll(faves);
            Log.e(TAG, "onFaveMoviesFetched: SIZE IS " + userFavesMovies.size());
            initRecyclerView();
        }
    };

    private void initFetchingMovies() {
        FetchAllMovies fetchAllMoviesTask = new FetchAllMovies(moviesInterface, database);
        fetchAllMoviesTask.execute();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        adapter = new RecyclerViewAdapterAllMovies(dummyMovies, userFavesMovies, this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    private void displayList(List<Movie> movies){
        adapter.setList(movies);
    }

    // AsyncTask to Fetch All Movies from DB
    private class FetchAllMovies extends AsyncTask<Void, Void, List<Movie>> {

        private FetchMovies fetchInterface;
        private DBUserMovie database;

        public FetchAllMovies(FetchMovies fetchInterface, DBUserMovie database) {
            this.fetchInterface = fetchInterface;
            this.database = database;
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {

            List<Movie> allMovies = database.getMovieDao().getAllMovies();

            return allMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> allMovies) {
            super.onPostExecute(allMovies);
            this.fetchInterface.onAllMoviesFetched(allMovies);
        }
    }

    // AsyncTask to Fetch Faves from DB

    private class FetchFaveMoviesTask extends AsyncTask<Long, Void, List<Movie>> {

        private FetchMovies fetchInterface;
        private DBUserMovie database;

        public FetchFaveMoviesTask(FetchMovies fetchInterface, DBUserMovie database) {
            this.fetchInterface = fetchInterface;
            this.database = database;
        }

        @Override
        protected List<Movie> doInBackground(Long... longs) {

            List<Movie> faveMovies = database.getUserMovieDao().getMoviesByUserId(longs[0]);

            return faveMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> faveMovies) {
            super.onPostExecute(faveMovies);
            Log.e(TAG, "FAVES SIZE IS " + faveMovies.size());
            this.fetchInterface.onFaveMoviesFetched(faveMovies);
        }
    }

}
