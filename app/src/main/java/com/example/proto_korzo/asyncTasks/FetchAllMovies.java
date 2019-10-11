package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public class FetchAllMovies extends AsyncTask<Void, Void, List<Movie>> {

    private AsyncTaskManager.TaskListener listener;
    private DBUserMovie database;

    public FetchAllMovies(DBUserMovie database, AsyncTaskManager.TaskListener listener) {
        this.database = database;
        this.listener = listener;
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {

        List<Movie> allMovies = database.getMovieDao().getAllMovies();

        return allMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> allMovies) {
        super.onPostExecute(allMovies);
        listener.onMoviesFetched(allMovies);
    }
}

