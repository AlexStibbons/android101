package com.example.proto_korzo.retrofit;

import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBService {

    @GET("movie/top_rated")
    Call<MovieListResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MovieListResponse> getPopularMovies(@Query("sort_by") String sort,
                                                   @Query("include_adult") boolean isAdult,
                                                   @Query("page") int page,
                                                   @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") int id, @Query("api_key") String apiKey);


}
