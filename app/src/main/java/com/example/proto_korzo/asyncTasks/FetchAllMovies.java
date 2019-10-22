package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.proto_korzo.Utils;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.MovieListResponse;
import com.example.proto_korzo.retrofit.API;
import com.example.proto_korzo.retrofit.MovieDBService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchAllMovies extends AsyncTask<Void, Void, List<Movie>> {

    private AsyncTaskManager.TaskListener listener;
    private DBUserMovie database;
    private MovieDBService movieService;

    public FetchAllMovies(DBUserMovie database, AsyncTaskManager.TaskListener listener) {
        this.database = database;
        this.listener = listener;
        this.movieService = API.getRetrofitInstance().create(MovieDBService.class);
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {

       List<Movie> allMovies = new ArrayList<>();
        try {
            MovieListResponse response = movieService.getPopularMovies(Utils.POPULARITY_DESC,false, 1, Utils.API_KEY).execute().body();
            allMovies = response.getResults();
        } catch (IOException e) {
            Log.e("Fetch BKG", "doInBackground: ERROR");
        }
        return allMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> allMovies) {
        super.onPostExecute(allMovies);
        listener.onMoviesFetched(allMovies);
    }
}

