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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMoviesFragment extends Fragment {

    private static final String TAG = AllMoviesFragment.class.getSimpleName();

    private int id; // userId

    private List<Movie> dummyMovies = new ArrayList<>();
    List<Movie> userFavesMovies = new ArrayList<>();
    List<Long> userFavesIds = new ArrayList<>();
    private DBUserMovie database;

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

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        database = DBUserMovie.getInstance(getActivity());
        id = getArguments().getInt("userId");

        // register broadcast receiver
        intentFilter = new IntentFilter(Utils.IF_FAVE_CHANGED);
        getContext().registerReceiver(br, intentFilter);

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        Log.e(TAG, "MOVIES FRAGMENT GOTTEN ID: " + id);
        TextView testy = rootView.findViewById(R.id.idView);
        testy.setText("User id is: " + id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);

        fetchMovies();

        return rootView;
    }

    private void fetchMovies() {

        AsyncTaskManager.fetchAllMovies(database, new AsyncTaskManager.TaskListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                dummyMovies.clear();
                dummyMovies.addAll(movies);
                Log.e(TAG, "onMoviesFetched: SIZE " + dummyMovies.size() );

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
                getActivity(), faveClickListener);

        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // ***** NO! Not like this *****
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
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
                    Toast.makeText(getActivity(), "End of list / Fetch new data", Toast.LENGTH_SHORT).show();

                    currentPage++;
                    MovieDBService movieService = API.getRetrofitInstance().create(MovieDBService.class);
                    Call<MovieListResponse> call = movieService.getPopularMovies(Utils.POPULARITY_DESC, false, currentPage, Utils.API_KEY);
                    call.enqueue(new Callback<MovieListResponse>() {
                        @Override
                        public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                            List<Movie> moreMovies = response.body().getResults();

                            adapter.addMovies(moreMovies);
                        }

                        @Override
                        public void onFailure(Call<MovieListResponse> call, Throwable t) {

                        }
                    });

                }
            }


        });


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

            // https://developer.android.com/guide/components/broadcasts#kotlin
            // For this reason, you should not! start long running background threads 
            // from a broadcast receiver. After onReceive(), the system can kill 
            // the process at any time to reclaim memory, and in doing so, 
            // it terminates the spawned thread running in the process. 
            // To avoid this, you should either call goAsync() (if you want a 
            // little more time to process the broadcast in a background thread) 
            // or schedule a JobService from the receiver using the JobScheduler, 
            // so the system knows that the process continues to perform active work.
            // SO
            // move call to AsyncTaskManager to separate method
            // and use that in onClick listener, before sending broadcast

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

    public static AllMoviesFragment getInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("userId", id);
        AllMoviesFragment allMoviesFragment = new AllMoviesFragment();
        allMoviesFragment.setArguments(args);
        return allMoviesFragment;
    };
}
