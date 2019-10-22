package com.example.proto_korzo.asyncTasks;


import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public class AsyncTaskManager {


    public static void fetchAllMovies(DBUserMovie database, TaskListener listener) {
        FetchAllMovies fetchAllMoviesTask = new FetchAllMovies(database, listener);
        fetchAllMoviesTask.execute();
    }


    public static void fetchFaveMovies(DBUserMovie database, int userId, TaskListener listener) {
        FetchFaveMovies fetchFaveMoviesTask = new FetchFaveMovies(database, listener);
        fetchFaveMoviesTask.execute(userId);
    }

    public static void setFaveMovie(DBUserMovie database, int userId, Movie movie, TaskListener listener) {
        AddFave addFaveTask = new AddFave(database, movie, listener);
        addFaveTask.execute(userId);
    }

    public static void removeFaveMovie(DBUserMovie database, int userId, Movie movie, TaskListener listener) {
        RemoveFave removeFaveTask = new RemoveFave(database, movie, listener);
        removeFaveTask.execute(userId);
    }

    public interface TaskListener {
        void onMoviesFetched(List<Movie> movies);
    }

    public interface OnFindUser {
        void onFinished(int userId);
    }

}
