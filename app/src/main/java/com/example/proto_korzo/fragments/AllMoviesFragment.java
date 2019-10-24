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
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.MovieListResponse;
import com.example.proto_korzo.retrofit.API;
import com.example.proto_korzo.retrofit.MovieDBService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMoviesFragment extends Fragment {

    private static final String TAG = AllMoviesFragment.class.getSimpleName();

    private int id; // userId

    private List<Movie> allMovies = new ArrayList<>();
    List<Movie> userFavesMovies = new ArrayList<>();
    List<Long> userFavesIds = new ArrayList<>();
    private DBUserMovie database;
    MovieDBService movieService;
    FloatingActionButton fab;

    RecyclerView recyclerView;
    private RecyclerViewAdapterAllMovies adapter;
    IntentFilter intentFilter;

    // for scroll
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    int currentPage = 1;

    public AllMoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allMovies.clear();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        database = DBUserMovie.getInstance(getActivity());
        movieService = API.getRetrofitInstance().create(MovieDBService.class);
        id = getArguments().getInt("userId");

        // register broadcast receiver
        intentFilter = new IntentFilter(Utils.IF_FAVE_CHANGED);
        getContext().registerReceiver(br, intentFilter);

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        fab = rootView.findViewById(R.id.fab);

        Log.e(TAG, "MOVIES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        fetchMovies(movieService);

        return rootView;
    }

    private void fetchMovies(MovieDBService movieService) {

        getMovies(currentPage, movieService); // page 1

        AsyncTaskManager.fetchFaveMovies(database, id, new AsyncTaskManager.TaskListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                userFavesMovies.clear();
                userFavesMovies.addAll(movies);
                initRecyclerView();
            }
        });

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        adapter = new RecyclerViewAdapterAllMovies(allMovies, userFavesMovies,
                getActivity(), faveClickListener);

        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        addOnScrollListener(recyclerView, layoutManager);

    }


    Listeners.OnFaveClick faveClickListener = new Listeners.OnFaveClick() {
        @Override
        public void onFave(Movie movie) {

            AsyncTaskManager.setFaveMovie(database, id, movie, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {

                }
            });

            getContext().sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));

        }

        @Override
        public void onUnfave(Movie movie) {
            AsyncTaskManager.removeFaveMovie(database, id, movie, new AsyncTaskManager.TaskListener() {
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
        public void onMovieItemClick(int movieId, boolean isFave) {

            Intent intent = new Intent(getContext(), MovieActivity.class);
            intent.putExtra("MovieIdExtra", movieId);
            intent.putExtra("IsFaveExtra", isFave);
            intent.putExtra("userIdExtra", id);

            startActivity(intent);
            // this fragment should stay alive, for going back
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

    private void getMovies(int currentPage, MovieDBService movieService) {

        Call<MovieListResponse> call = movieService.getPopularMovies(Utils.POPULARITY_DESC, false, currentPage, Utils.API_KEY);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {

                List<Movie> moreMovies = response.body().getResults();
                adapter.addMovies(moreMovies);
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error when fetching movies", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addOnScrollListener(RecyclerView recyclerView, final LinearLayoutManager layoutManager) {
        // ***** very untidy *****
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    fab.hide();
                }

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    fab.show();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    currentPage++;
                    getMovies(currentPage, movieService);
                }
            }
        });
    }

    private void broadcastAction() {
        AsyncTaskManager.fetchFaveMovies(database, id, new AsyncTaskManager.TaskListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                adapter.setFaveMovies(movies);
            }
        });
    }

    public static AllMoviesFragment getInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("userId", id);
        AllMoviesFragment allMoviesFragment = new AllMoviesFragment();
        allMoviesFragment.setArguments(args);
        return allMoviesFragment;
    }

}
