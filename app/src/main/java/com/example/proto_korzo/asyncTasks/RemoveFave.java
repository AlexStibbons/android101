package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public class RemoveFave extends AsyncTask<Integer, Void, List<Movie>> {

    private DBUserMovie database;
    private AsyncTaskManager.TaskListener listener;
    private Movie movie;

    public RemoveFave(DBUserMovie database, Movie movie, AsyncTaskManager.TaskListener listener) {
        this.database = database;
        this.listener = listener;
        this.movie = movie;
    }

    @Override
    protected List<Movie> doInBackground(Integer... ints) {
        // delete(userId, movieID)
        database.getUserMovieDao().deleteByMovieAndUserId(ints[0], movie.getId());
        database.getMovieDao().deleteMovie(movie);

        List<Movie> newFaves = database.getUserMovieDao().getMoviesByUserId(ints[0]);

        return newFaves;
    }

    @Override
    protected void onPostExecute(List<Movie> newFaves) {
        super.onPostExecute(newFaves);

        listener.onMoviesFetched(newFaves);
    }
}
