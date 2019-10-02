package com.example.proto_korzo.database;

public class MovieDBAPI {

    private static final String API_KEY = "";

    private static MovieDBAPI instance = new MovieDBAPI("");

    private MovieDBAPI(String key){

    }

    public static MovieDBAPI getInstance(){
        return instance;
    }

    public void requests(){

    }
}
