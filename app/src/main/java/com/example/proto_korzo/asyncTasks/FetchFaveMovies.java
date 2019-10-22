package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public class FetchFaveMovies extends AsyncTask<Integer, Void, List<Movie>> {

    private DBUserMovie database;
    private AsyncTaskManager.TaskListener listener;

    public FetchFaveMovies(DBUserMovie database, AsyncTaskManager.TaskListener listener) {
        this.database = database;
        this.listener = listener;
    }

    @Override
    protected List<Movie> doInBackground(Integer... ints) {

        List<Movie> faveMovies = database.getUserMovieDao().getMoviesByUserId(ints[0]);

        return faveMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> faveMovies) {
        super.onPostExecute(faveMovies);

        listener.onMoviesFetched(faveMovies);
    }
}
