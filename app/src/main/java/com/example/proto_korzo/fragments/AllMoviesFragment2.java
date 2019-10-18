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

public class AllMoviesFragment2 extends Fragment {

    private static final String TAG = AllMoviesFragment2.class.getSimpleName();

    private long id; // userId

    private List<Movie> dummyMovies = new ArrayList<>();
    List<Movie> userFavesMovies = new ArrayList<>();
    List<Long> userFavesIds = new ArrayList<>();
    private DBUserMovie database;

    RecyclerView recyclerView;
    private RecyclerViewAdapterAllMovies adapter;
    FaveChangeBroadcast faveReceiver;
    IntentFilter intentFilter;

    public AllMoviesFragment2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = DBUserMovie.getInstance(getActivity());
        Bundle arguments = getArguments();
        this.id = arguments.getLong("userId");

        // register broadcast receiver
        intentFilter = new IntentFilter(Utils.IF_FAVE_CHANGED);
        getContext().registerReceiver(br, intentFilter);

        fetchMovies();
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


        Log.e(TAG, "onViewCreated: ALL LIST SIZE" + dummyMovies.size());
        Log.e(TAG, "onViewCreated: FAVE LIST SIZE" + userFavesMovies.size());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);
        initRecyclerView();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void fetchMovies() {

        Log.e(TAG, "fetchMovies: STARTED");
        AsyncTaskManager.fetchAllMovies(database, new AsyncTaskManager.TaskListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                dummyMovies.clear();
                dummyMovies.addAll(movies);
                Log.e(TAG, "onMoviesFetched: ALL SIZE " + dummyMovies.size());

                // now start another async task
                AsyncTaskManager.fetchFaveMovies(database, id, new AsyncTaskManager.TaskListener() {
                    @Override
                    public void onMoviesFetched(List<Movie> movies) {
                        userFavesMovies.clear();
                        userFavesMovies.addAll(movies);
                    }
                });

            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");


        adapter = new RecyclerViewAdapterAllMovies(dummyMovies, userFavesMovies,
                this.getActivity(), faveClickListener);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }


    Listeners.OnFaveClick faveClickListener = new Listeners.OnFaveClick() {
        @Override
        public void onFave(long movieId) {

            AsyncTaskManager.setFaveMovie(database, id, movieId, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {

                }
            });

            getContext().sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));

        }

        @Override
        public void onUnfave(long movieId) {
            AsyncTaskManager.removeFaveMovie(database, id, movieId, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    // userFavesMovies.clear();
                    // userFavesMovies.addAll(movies);
                    // adapter.setFaveMovies(movies);
                }
            });

            getContext().sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));

        }

        @Override
        public void onMovieItemClick(long movieId, boolean isFave) {

            Intent intent = new Intent(getContext(), MovieActivity.class);
            intent.putExtra("MovieIdExtra", movieId);
            intent.putExtra("IsFaveExtra", isFave);
            intent.putExtra("userIdExtra", id);

            startActivity(intent);
            // this fragment should stay alive, for going back
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
                AsyncTaskManager.fetchFaveMovies(database, id, new AsyncTaskManager.TaskListener() {
                    @Override
                    public void onMoviesFetched(List<Movie> movies) {
                       adapter.setFaveMovies(movies);
                    }
                });
            }

        }
    };

    public static AllMoviesFragment2 getInstance(long id) {
        Bundle args = new Bundle();
        args.putLong("userId", id);
        AllMoviesFragment2 allMoviesFragment2 = new AllMoviesFragment2();
        allMoviesFragment2.setArguments(args);
        return allMoviesFragment2;
    }

}
