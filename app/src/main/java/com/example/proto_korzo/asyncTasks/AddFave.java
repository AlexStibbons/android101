package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.UserMovieJoin;

import java.util.List;

public class AddFave extends AsyncTask<Long, Void, List<Movie>> {

    private DBUserMovie database;
    private AsyncTaskManager.TaskListener listener;

    public AddFave(DBUserMovie database, AsyncTaskManager.TaskListener listener){
        this.database = database;
        this.listener = listener;
    }

    @Override
    protected List<Movie> doInBackground(Long... longs) {
        // UserMovieJoin(userId, movieId)
        database.getUserMovieDao().addUserMovie(new UserMovieJoin(longs[0], longs[1]));
        List<Movie> newFaves = database.getUserMovieDao().getMoviesByUserId(longs[0]);

        return newFaves;
    }

    @Override
    protected void onPostExecute(List<Movie> newFaves) {
        super.onPostExecute(newFaves);

        listener.onMoviesFetched(newFaves);
    }
}
