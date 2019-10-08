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
import com.example.proto_korzo.database.model.User;

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

            userId = database.getUserDao().getUserIdByEmail(strings[0]);

            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            super.onPostExecute(userId);
            Log.e(TAG, "FOUND USER ID IS " + userId);

            if (this.userInterface != null ) {
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

        public CreateUserTask(long userId, DBUserMovie database, FindUser usInterface){
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
