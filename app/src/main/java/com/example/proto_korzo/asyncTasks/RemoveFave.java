package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

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
        //  @Query("DELETE FROM user_movie WHERE user_movie.movieId = :movieId AND user_movie.userId = :userId")
        //    public void deleteByMovieAndUserId(int movieId, int userId);
        Log.e("REMOVE TASK", "doInBackground: MOVIE IS " + movie.getTitle() + " ID IS " + movie.getId());
        database.getUserMovieDao().deleteByMovieAndUserId(movie.getId(), ints[0]);

        List<Movie> newFaves = database.getUserMovieDao().getMoviesByUserId(ints[0]);

        return newFaves;
    }

    @Override
    protected void onPostExecute(List<Movie> newFaves) {
        super.onPostExecute(newFaves);

        listener.onMoviesFetched(newFaves);
    }
}
