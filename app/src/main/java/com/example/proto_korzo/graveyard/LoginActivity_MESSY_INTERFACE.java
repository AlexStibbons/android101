package com.example.proto_korzo.graveyard;

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
import com.example.proto_korzo.activities.ListsActivity;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.User;

public class LoginActivity_MESSY_INTERFACE extends AppCompatActivity  {

    private static final String TAG = "LoginActivity_MESSY_INTERFACE";
    private static final String EXTRA_ID = "com.example.proto_korzo.LoginActivity_MESSY_INTERFACE.UserId";

    // declare necessary variables
    EditText emailInput;
    EditText passwordInput;
    Button enter;

    long userId;
    private DBUserMovie database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        // find the variables in the layout
        emailInput = (EditText) findViewById(R.id.login_email);
        passwordInput = (EditText) findViewById(R.id.login_password);
        enter = (Button) findViewById(R.id.btn_enter);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get input values
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // if and only if the inputs are valid, get an instance of the
                // database, and
                // start the process of finding/creating the user
                if (Utils.hasLoginData(email, password)) {
                    database = DBUserMovie.getInstance(LoginActivity_MESSY_INTERFACE.this);
                    findUser(email, password);
                } else {
                    Toast.makeText(LoginActivity_MESSY_INTERFACE.this,
                            "Please, enter email and password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // this one starts the chain of going down async tasks
    public void findUser(String email, String password){
        GetUserTask findUser = new GetUserTask(userId, database, findUserInterface);
        findUser.execute(email);
    }

    // an intent that needs to:
    // 1. change activity once the user is valid
    // 2. close the login activity
    public void intentListsActivity(long userId){

        Log.e(TAG, "CHANGE ACT USER ID IS " + userId );
        Intent intent = new Intent(LoginActivity_MESSY_INTERFACE.this, ListsActivity.class);
        intent.putExtra(EXTRA_ID, userId);
        startActivity(intent);
        finish();
    }

    // this is the interface [nested in the GetUser Async Task]
    // created outside the task, so the methods can access all the necessary
    // varibles/methods
    GetUserTask.FindUser findUserInterface = new GetUserTask.FindUser() {
        @Override
        public void userFound(long id) {
            Log.e(TAG, "INTERFACE ID: " + id);
            intentListsActivity(id);
        }

        @Override
        public void userNotFound() {
            String email2 = emailInput.getText().toString();
            String password2 = passwordInput.getText().toString();
            CreateUserTask createUser = new CreateUserTask(userId, database, findUserInterface);
            createUser.execute(email2, password2);
        }
    };

    // the first async task - find an existing user
    private static class GetUserTask extends AsyncTask<String, Void, Long>{

        public interface FindUser {

            void userFound(long id);
            void userNotFound();
        }

        private final FindUser findUserInterface;
        private long userId;
        private DBUserMovie database;

        GetUserTask(long userId, DBUserMovie database, FindUser findUserInterface){
            this.userId = userId;
            this.database = database;
            this.findUserInterface = findUserInterface;
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

            if (this.findUserInterface != null) {

                if (userId > 0) {
                    this.findUserInterface.userFound(userId);
                } else {
                    // this starts another async task
                    // as seen above, on overriding interface's methods
                    this.findUserInterface.userNotFound();
                }

            }

        }
    }

    private static class CreateUserTask extends AsyncTask<String, Void, Long> {

        private long userId;
        private DBUserMovie database;
        private GetUserTask.FindUser findUserInterface;

        CreateUserTask(long userId, DBUserMovie database, GetUserTask.FindUser findUserInterface) {
            this.userId = userId;
            this.database = database;
            this.findUserInterface = findUserInterface;
        }

        @Override
        protected Long doInBackground(String... strings) {
            userId = database.getUserDao().addUser(new User(strings[0], strings[1]));

            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            super.onPostExecute(userId);
            // since the user is definitely presend, and we got the user's Id
            // call the userFound method of the interface
            // the userFound method calls the intent which opens
            // a new activity and closes this one
            Log.e(TAG, "CREATED USER ID IS " + userId);
            this.findUserInterface.userFound(userId);
        }
    }

}
