package com.example.proto_korzo.fragments;

public class Listeners {

    public interface OnFaveClick {
        void onFave(long movieId);
        void onUnfave(long movieId);
        void onMovieItemClick(long movieId, boolean isFave);
    }

    // add default methods in interface?
    // https://www.baeldung.com/java-static-default-methods
    // no, because async tasks need to post execute in the activity/fragment itself

}
