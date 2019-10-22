package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.List;

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

    public void fetchMovieById(int movieId){
        FetchMovieTask fetchMovie = new FetchMovieTask(database, fetchMovieInterface);
        fetchMovie.execute((int) movieId);
    }

    private interface FetchMovie {
        void onMovieFetched(Movie movie);
    }

    FetchMovie fetchMovieInterface = new FetchMovie() {
        @Override
        public void onMovieFetched(final Movie movie) {

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
    };

    private class FetchMovieTask extends AsyncTask<Integer, Void, Movie> {

        private FetchMovie fetchInterface;
        private DBUserMovie database;
        private MovieDBService movieService;

        public FetchMovieTask(DBUserMovie database, FetchMovie fetchInterface) {
            this.database = database;
            this.fetchInterface = fetchInterface;
            this.movieService = API.getRetrofitInstance().create(MovieDBService.class);
        }

        @Override
        protected Movie doInBackground(Integer... ints) {

            //Movie foundMovie = database.getMovieDao().getMovieById(longs[0]);
            Movie foundMovie = null;
            try {
                foundMovie = movieService.getMovie(ints[0], Utils.API_KEY).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return foundMovie;
        }

        @Override
        protected void onPostExecute(Movie foundMovie) {
            super.onPostExecute(foundMovie);
            this.fetchInterface.onMovieFetched(foundMovie);
        }
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
            // do nothiiiinggggg
        }
    };


}
