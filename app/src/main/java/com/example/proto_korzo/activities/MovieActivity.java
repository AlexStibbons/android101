package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proto_korzo.R;
import com.example.proto_korzo.Utils;
import com.example.proto_korzo.asyncTasks.AsyncTaskManager;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.fragments.Listeners;
import com.example.proto_korzo.retrofit.API;
import com.example.proto_korzo.retrofit.MovieDBService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {

    private int userId;
    private int movieId;
    private boolean isFave;
    private DBUserMovie database;

    TextView movieTitle;
    TextView movieDescription;
    ImageView movieImage;
    ToggleButton btnFave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_movie);

        database = DBUserMovie.getInstance(this);

        movieTitle = findViewById(R.id.et_movieAct_title);
        movieDescription = findViewById(R.id.et_movieAct_description);
        movieImage = findViewById(R.id.img_movieAct_image);
        btnFave = findViewById(R.id.btn_movieAct_favorite);

        Intent i = getIntent();
        movieId = i.getIntExtra("MovieIdExtra", -1);
        isFave = i.getBooleanExtra("IsFaveExtra", false);
        userId = i.getIntExtra("userIdExtra", -1);

        Log.e("movie activity", "onCreate: movie id " + movieId);
        Log.e("movie activity", "onCreate: is fave " + isFave);
        Log.e("movie activity", "onCreate: user id " + userId);

        fetchMovieById(movieId);

    }

    public void fetchMovieById(int movieId) {

        MovieDBService movieService = API.getRetrofitInstance().create(MovieDBService.class);
        Call<Movie> movieCall = movieService.getMovie(movieId, Utils.API_KEY);

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie fetchedMovie = response.body();
                onMovieFetched(fetchedMovie);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MovieActivity.this, "Error during fetching movie",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    Listeners.OnFaveClick onFaveInterface = new Listeners.OnFaveClick() {
        @Override
        public void onFave(Movie movie) {
            AsyncTaskManager.setFaveMovie(database, userId, movie, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));
                }
            });
        }

        @Override
        public void onUnfave(Movie movie) {
            AsyncTaskManager.removeFaveMovie(database, userId, movie, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));
                }
            });
        }

        @Override
        public void onMovieItemClick(int movieId, boolean isFave) {

        }
    };

    private void onMovieFetched(final Movie movie) {
        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getOverview());

        Glide.with(MovieActivity.this)
                .asBitmap()
                .load(Utils.BASE_IMG_URL + movie.getPoster_path())
                .into(movieImage);

        btnFave.setOnCheckedChangeListener(null);

        if (isFave) {
            btnFave.setChecked(true);
        } else {
            btnFave.setChecked(false);
        }

        btnFave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onFaveInterface.onFave(movie);
                } else {
                    onFaveInterface.onUnfave(movie);
                }
            }
        });

    }
}

