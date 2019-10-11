package com.example.proto_korzo.fragments;

public class Listeners {

    public interface OnFaveClick {
        void onFave(long movieId);
        void onUnfave(long movieId);
    }

}
