package com.example.proto_korzo.retrofit;

import com.example.proto_korzo.Utils;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.MovieListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallManager {

    private static MovieDBService movieService = API.getRetrofitInstance().create(MovieDBService.class);

    public static void getPopularMovies(int currentPage, final onApiFetchedMovies listener) {
        Call<MovieListResponse> call = movieService.getPopularMovies(Utils.POPULARITY_DESC, false,
                currentPage, Utils.API_KEY);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                List<Movie> movies = response.body().getResults();
                listener.onMoviesFetched(movies);
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                listener.onFailure("Error while fetching movies");
            }
        });

    }

    public static void getOneMovie(int movieId, final onApiFetchedOne listener) {

        Call<Movie> call = movieService.getMovie(movieId, Utils.API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie fetched = response.body();
                listener.onFetchedMovie(fetched);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                listener.onFailure("Error while fetching movie.");
            }
        });

    }

    // placeholder for discover search
    public static void discoverMovies(String queries, final onApiFetchedMovies listener) {

        // discover search has a lot of queries, but response similar to getPopularMovies
        // query object likely necessary:
        // queries are:
        // 1. list of genre ids
        // 2. greater than date String, for ex: "1900-01-01"
        // 3. lesser than date String, for ex: "1940-01-01"
        // 4. sort by string
        // 5. page number
        // URL is:
        // https://api.themoviedb.org/3/discover/movie
        // ?api_key=apikey
        // &sort_by=popularity.desc
        // &include_adult=false
        // &page=1
        // &primary_release_date.gte=1900-01-01
        // &primary_release_date.lte=1940-01-01
        // &with_genres=35%2C18

    }


    public interface onApiFetchedMovies {
        void onMoviesFetched(List<Movie> movies);
        void onFailure(String errorText);
    }

    public interface onApiFetchedOne {
        void onFetchedMovie(Movie movie);
        void onFailure(String errorText);
    }

}
