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

import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private long userId;
    private long movieId;
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
        movieId = i.getLongExtra("MovieIdExtra", -1);
        isFave = i.getBooleanExtra("IsFaveExtra", false);
        userId = i.getLongExtra("userIdExtra", -1);

        Log.e("movie activity", "onCreate: movie id " + movieId);
        Log.e("movie activity", "onCreate: is fave " + isFave);
        Log.e("movie activity", "onCreate: user id " + userId);

        fetchMovieById(movieId);
    }

    public void fetchMovieById(long movieId){
        FetchMovieTask fetchMovie = new FetchMovieTask(database, fetchMovieInterface);
        fetchMovie.execute(movieId);
    }

    private interface FetchMovie {
        void onMovieFetched(Movie movie);
    }

    FetchMovie fetchMovieInterface = new FetchMovie() {
        @Override
        public void onMovieFetched(final Movie movie) {

            movieTitle.setText(movie.getTitle());
            movieDescription.setText(movie.getDescription());

            Glide.with(MovieActivity.this)
                    .asBitmap()
                    .load(movie.getImgUrl())
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
                        onFaveInterface.onFave(movieId);
                    } else {
                        onFaveInterface.onUnfave(movieId);
                    }
                }
            });

        }
    };

    private class FetchMovieTask extends AsyncTask<Long, Void, Movie> {

        private FetchMovie fetchInterface;
        private DBUserMovie database;

        public FetchMovieTask(DBUserMovie database, FetchMovie fetchInterface) {
            this.database = database;
            this.fetchInterface = fetchInterface;
        }

        @Override
        protected Movie doInBackground(Long... longs) {

            Movie foundMovie = database.getMovieDao().getMovieById(longs[0]);

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
        public void onFave(long movieId) {
            AsyncTaskManager.setFaveMovie(database, userId, movieId, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));
                }
            });
        }

        @Override
        public void onUnfave(long movieId) {

            AsyncTaskManager.removeFaveMovie(database, userId, movieId, new AsyncTaskManager.TaskListener() {
                @Override
                public void onMoviesFetched(List<Movie> movies) {
                    sendBroadcast(new Intent(Utils.IF_FAVE_CHANGED));
                }
            });

        }

        @Override
        public void onMovieItemClick(long movieId, boolean isFave) {
            // do nothiiiinggggg
        }
    };


}
