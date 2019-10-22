package com.example.proto_korzo.retrofit;

import com.example.proto_korzo.database.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TvDBService {

    @GET("tv/{id}")
    Call<Movie> getTvShowById(@Query("api_key") String apiKey, @Path("id") int id);
}
