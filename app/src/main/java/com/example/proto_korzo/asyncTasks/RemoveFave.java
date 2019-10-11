package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public class RemoveFave extends AsyncTask<Long, Void, List<Movie>> {

    private DBUserMovie database;
    private AsyncTaskManager.TaskListener listener;

    public RemoveFave(DBUserMovie database, AsyncTaskManager.TaskListener listener) {
        this.database = database;
        this.listener = listener;
    }

    @Override
    protected List<Movie> doInBackground(Long... longs) {
        // delete(userId, movieID)
        database.getUserMovieDao().deleteByMovieAndUserId(longs[0], longs[1]);

        List<Movie> newFaves = database.getUserMovieDao().getMoviesByUserId(longs[0]);

        return newFaves;
    }

    @Override
    protected void onPostExecute(List<Movie> newFaves) {
        super.onPostExecute(newFaves);

        listener.onMoviesFetched(newFaves);
    }
}
