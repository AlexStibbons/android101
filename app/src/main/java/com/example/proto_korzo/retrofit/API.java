package com.example.proto_korzo.retrofit;

import com.example.proto_korzo.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            retrofit =  new Retrofit.Builder()
                    .baseUrl(Utils.BASE_MOVIEDB_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    };

}
