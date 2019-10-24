package com.example.proto_korzo.retrofit;

import com.example.proto_korzo.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static Retrofit retrofit;
    private static MovieDBService movieService;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            retrofit =  new Retrofit.Builder()
                    .baseUrl(Utils.BASE_MOVIEDB_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    };

    // yes or no?
    public static MovieDBService getMovieServiceInstance(){

        if (retrofit == null ) {
            movieService =  new Retrofit.Builder()
                    .baseUrl(Utils.BASE_MOVIEDB_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MovieDBService.class);
        }

        if (movieService == null ) {
            movieService = retrofit.create(MovieDBService.class);
        }

       return movieService;
    }

}
