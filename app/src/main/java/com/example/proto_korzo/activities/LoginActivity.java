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
        void userFound(int id);

        void userNotFound();
    }

    // *** needed variables ***
    EditText etEmail;
    EditText etPassword;
    Button enter;

    int userId;
    private DBUserMovie database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_login);

        //  SqlScoutServer.create(this, getPackageName());

        etEmail = findViewById(R.id.login_email);
        etPassword = findViewById(R.id.login_password);
        enter = findViewById(R.id.btn_enter);

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

    FindUser usInterface = new FindUser() {
        @Override
        public void userFound(int id) {
            Log.e(TAG, "INTERFACE USER ID IS " + id);
            changeActivity(id);
        }

        @Override
        public void userNotFound() {
            String emailForCreation = etEmail.getText().toString();
            String passwordForCreation = etPassword.getText().toString();
            CreateUserTask createUser = new CreateUserTask(userId, database, usInterface);
            createUser.execute(emailForCreation, passwordForCreation);
        }
    };

    public void fetchUser(String email, String password) {
        GetUserTask getUser = new GetUserTask(userId, database, usInterface);
        getUser.execute(email, password);
    }

    public void changeActivity(int userId) {

        Log.e(TAG, "CHANGE ACT USER ID " + userId);

        Intent intent = new Intent(LoginActivity.this, ListsActivity.class);
        intent.putExtra(EXTRA_ID, userId);
        startActivity(intent);
        //finish();
    }

    private static class GetUserTask extends AsyncTask<String, Void, Integer> {

        private FindUser userInterface;
        private int userId;
        private DBUserMovie database;

        public GetUserTask(int userId, DBUserMovie database, FindUser userInterface) {
            this.userId = userId;
            this.database = database;
            this.userInterface = userInterface;
        }

        @Override
        protected Integer doInBackground(String... strings) {

            Long id = database.getUserDao().getUserIdByEmail(strings[0]);

            userId = id.intValue();

            return userId;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

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

    private static class CreateUserTask extends AsyncTask<String, Void, Integer> {

        private int userId;
        private DBUserMovie database;
        private FindUser usInterface;

        public CreateUserTask(int userId, DBUserMovie database, FindUser usInterface) {
            this.userId = userId;
            this.database = database;
            this.usInterface = usInterface;
        }


        @Override
        protected Integer doInBackground(String... strings) {

            Long id = database.getUserDao().addUser(new User(strings[0], strings[1]));

            userId = id.intValue();

            return userId;
        }

        @Override
        protected void onPostExecute(Integer userId) {
            super.onPostExecute(userId);
            Log.e(TAG, "CREATED USER ID IS " + userId);
            if (this.usInterface != null) {
                this.usInterface.userFound(userId);
            }
        }
    }
}

