package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.UserMovieJoin;

import java.util.List;

public class AddFave extends AsyncTask<Integer, Void, List<Movie>> {

    private DBUserMovie database;
    private AsyncTaskManager.TaskListener listener;
    private Movie movie;

    public AddFave(DBUserMovie database, Movie movie, AsyncTaskManager.TaskListener listener){
        this.database = database;
        this.listener = listener;
        this.movie = movie;
    }

    @Override
    protected List<Movie> doInBackground(Integer... ints) {
        // UserMovieJoin(userId, movieId)
        database.getMovieDao().addMovie(movie);
        database.getUserMovieDao().addUserMovie(new UserMovieJoin(ints[0], movie.getId()));
        List<Movie> newFaves = database.getUserMovieDao().getMoviesByUserId(ints[0]);
        return newFaves;
    }

    @Override
    protected void onPostExecute(List<Movie> newFaves) {
        super.onPostExecute(newFaves);

        listener.onMoviesFetched(newFaves);
    }
}
