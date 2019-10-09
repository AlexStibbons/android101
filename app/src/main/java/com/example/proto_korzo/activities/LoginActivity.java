package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proto_korzo.R;
import com.example.proto_korzo.Utils;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.User;
import com.example.proto_korzo.database.model.UserMovieJoin;

import java.util.List;

// Login with interface callback
// same as Login2, but more understandable
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static final String EXTRA_ID = "com.example.proto_korzo.LoginActivity.UserId";

    // before on create, what will this activity need?
    // 1. variables, including DB instance and one for the value that needs to be passed on
    // 2. since there will be two async tasks, an interface is useful for callback

    // *** interface ***
    public interface FindUser {
        void userFound(long id);

        void userNotFound();
    }

    // *** needed variables ***
    EditText etEmail;
    EditText etPassword;
    Button enter;

    long userId;
    private DBUserMovie database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate view
        setContentView(R.layout.act_main);

        // now find the variables
        etEmail = findViewById(R.id.login_email);
        etPassword = findViewById(R.id.login_password);
        enter = findViewById(R.id.btn_enter);

        // once the button is clicked
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (Utils.hasLoginData(email, password)) {

                    database = DBUserMovie.getInstance(LoginActivity.this);
                    fetchUser(email, password); // starting point
                } else {
                    Toast.makeText(LoginActivity.this, "Email and password, please",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    // the created interface
    // since this interface needs to be present in both GetUser task and CreateUser taks
    // better to define it like so, than to make it anonymous in task constructor
    FindUser usInterface = new FindUser() {
        @Override
        public void userFound(long id) {
            // change activity
            Log.e(TAG, "INTERFACE USER ID IS " + id);
            changeActivity(id);
        }

        @Override
        public void userNotFound() {
            // start another async task to create a user
            String emailForCreation = etEmail.getText().toString();
            String passwordForCreation = etPassword.getText().toString();
            CreateUserTask createUser = new CreateUserTask(userId, database, usInterface);
            createUser.execute(emailForCreation, passwordForCreation);
        }
    };

    // some more helper methods
    public void fetchUser(String email, String password) {
        GetUserTask getUser = new GetUserTask(userId, database, usInterface);
        getUser.execute(email, password);
    }

    public void changeActivity(long userId) {
        // create intent
        Log.e(TAG, "CHANGE ACT USER ID " + userId);
        Intent intent = new Intent(LoginActivity.this, ListsActivity.class);
        // pass value with intent
        intent.putExtra(EXTRA_ID, userId);
        // start new activity via intent, obvs
        startActivity(intent);
        // finish this activity
        finish();
    }

    // finally, the two async tasks the interface works with
    private static class GetUserTask extends AsyncTask<String, Void, Long> {

        // this taks will need: userId, database, interface
        private FindUser userInterface;
        private long userId;
        private DBUserMovie database;

        public GetUserTask(long userId, DBUserMovie database, FindUser userInterface) {
            this.userId = userId;
            this.database = database;
            this.userInterface = userInterface;
        }

        @Override
        protected Long doInBackground(String... strings) {

            // init dummy movies here

            // if movie list is zero or isEmpty
            List<Movie> dbMovies = database.getMovieDao().getAllMovies();
            if (dbMovies.size() < 1) {
                // create movies
                // add to database
                String img1 = "https://i2.wp.com/www.tor.com/wp-content/uploads/2019/09/vetting-final.jpg?fit=740%2C1067&type=vertical&quality=100&ssl=1";
                String img2 = "https://i0.wp.com/www.tor.com/wp-content/uploads/2019/07/forhecancreep_full.jpg?fit=740%2C777&type=vertical&quality=100&ssl=1";
                String img3 = "https://i1.wp.com/www.tor.com/wp-content/uploads/2019/09/Hundrethhouse_-full.jpeg?fit=740%2C958&type=vertical&quality=100&ssl=1";
                String img4 = "http://strangehorizons.com/wordpress/wp-content/uploads/2019/07/regret-return_600px.png";
                String img5 = "http://strangehorizons.com/wordpress/wp-content/uploads/2019/08/FullSomedayWeWill-402x500.png";

                Movie[] input = {
                        new Movie(1, "Title 1 ", "desc 1", img1),
                        new Movie(256, "Title 2", "desc 2", img2),
                        new Movie(2, "Title 3", "desc 3", img3),
                        new Movie(5689, "Title 4", "desc 4", img4),
                        new Movie(98, "Title 5", "desc 5", img5),
                        new Movie(123, "Title 6", "desc 6", img1),
                        new Movie(555, "Title 7", "desc 7", img2),
                        new Movie(95674, "Title 8", "desc 8", img3),
                        new Movie(15, "Title 9", "desc 9", img4),
                        new Movie(97, "Title 10", "desc 10", img5),
                        new Movie(675, "Title 11", "desc 11", img1),
                        new Movie(624, "Title 12", "desc 12", img2),
                        new Movie(999, "Title 13", "desc 13", img3),
                        new Movie(956, "Title 14", "desc 14", img4),
                        new Movie(12567, "Title 15", "desc 15", img5)};

                database.getMovieDao().addMovieList(input);
            }

            userId = database.getUserDao().getUserIdByEmail(strings[0]);

            // one dummy favourite
            if (userId > 0) {
                database.getUserMovieDao().addUserMovie(new UserMovieJoin(userId, 15));
            }

            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            super.onPostExecute(userId);
            Log.e(TAG, "FOUND USER ID IS " + userId);

            if (this.userInterface != null) {
                if (userId > 0) {
                    this.userInterface.userFound(userId);
                } else {
                    this.userInterface.userNotFound();
                }
            }
        }
    }

    private static class CreateUserTask extends AsyncTask<String, Void, Long> {

        // again this task needs: userId, database, interface
        private long userId;
        private DBUserMovie database;
        private FindUser usInterface;

        public CreateUserTask(long userId, DBUserMovie database, FindUser usInterface) {
            this.userId = userId;
            this.database = database;
            this.usInterface = usInterface;
        }


        @Override
        protected Long doInBackground(String... strings) {

            userId = database.getUserDao().addUser(new User(strings[0], strings[1]));

            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            super.onPostExecute(userId);
            Log.e(TAG, "CREATED USER ID IS " + userId);
            if (this.usInterface != null) {
                this.usInterface.userFound(userId);
            }
        }
    }
}
