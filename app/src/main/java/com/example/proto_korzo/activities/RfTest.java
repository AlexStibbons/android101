package com.example.proto_korzo.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proto_korzo.R;
import com.example.proto_korzo.Utils;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.MovieListResponse;
import com.example.proto_korzo.retrofit.API;
import com.example.proto_korzo.retrofit.MovieDBService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RfTest extends AppCompatActivity {

    private static final String TAG = RfTest.class.getSimpleName();
    private MovieDBService movieService;
    private TextView viewMovies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rftest);

        viewMovies = findViewById(R.id.rf_text);
        viewMovies.setText("THE LIST:\n");

        Retrofit retrofit = API.getRetrofitInstance();

        movieService = retrofit.create(MovieDBService.class);

        Call<MovieListResponse> getPop = movieService.getPopularMovies(Utils.POPULARITY_DESC,
                false, 1, Utils.API_KEY);

        Log.e(TAG, "onCreate: CALLING ENQUEUE");

        getPop.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {

                List<Movie> movies = response.body().getResults();

               /* movies.stream()
                        .forEach(m ->
                                viewMovies.append("Title: " + m.getTitle() +
                                "\nDescription: " + m.getOverview() +
                                "\nImdb id: " + m.getImdb_id() +
                                "\nPoster path: " + m.getPoster_path() +
                                "\nId: " + m.getId() + "\n\n");

                        );*/

                for (Movie movie : movies) {
                    String content = "Title: " + movie.getTitle() +
                            "\nDescription: " + movie.getOverview() +
                            "\nImdb id: " + movie.getImdb_id() +
                            "\nPoster path: " + movie.getPoster_path() +
                            "\nId: " + movie.getId() + "\n\n";

                    viewMovies.append(content);

                }
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {

            }
        });
    }
}


