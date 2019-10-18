package com.example.proto_korzo.asyncTasks;


import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public class AsyncTaskManager {

    // should it be singleton? [getInstance, then setDatabase]
    // then declare all tasks, so they could be cancelled?

    public static void fetchAllMovies(DBUserMovie database, TaskListener listener) {
        FetchAllMovies fetchAllMoviesTask = new FetchAllMovies(database, listener);
        fetchAllMoviesTask.execute();
    }


    public static void fetchFaveMovies(DBUserMovie database, long userId, TaskListener listener) {
        FetchFaveMovies fetchFaveMoviesTask = new FetchFaveMovies(database, listener);
        fetchFaveMoviesTask.execute(userId);
    }

    public static void setFaveMovie(DBUserMovie database, long userId, long movieId, TaskListener listener) {
        AddFave addFaveTask = new AddFave(database, listener);
        addFaveTask.execute(userId, movieId);
    }

    public static void removeFaveMovie(DBUserMovie database, long userId, long movieId, TaskListener listener) {
        RemoveFave removeFaveTask = new RemoveFave(database, listener);
        removeFaveTask.execute(userId, movieId);
    }

    public interface TaskListener {
        void onMoviesFetched(List<Movie> movies);
    }

    public interface OnFindUser {
        void onFinished(long userId);
    }

}
