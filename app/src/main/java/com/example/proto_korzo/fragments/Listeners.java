package com.example.proto_korzo.fragments;

import com.example.proto_korzo.database.model.Movie;

public class Listeners {

    public interface OnFaveClick {
        void onFave(Movie movie);
        void onUnfave(Movie movie);
        void onMovieItemClick(int movieId, boolean isFave);
    }

    // add default methods in interface?
    // https://www.baeldung.com/java-static-default-methods
    // no, because async tasks need to post execute in the activity/fragment itself

}
