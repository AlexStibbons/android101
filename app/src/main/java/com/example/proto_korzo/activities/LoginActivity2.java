package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class LoginActivity2 extends AppCompatActivity  {

    private static final String TAG = "LoginActivity with interface";
    private static final String EXTRA_ID = "com.example.proto_korzo.LoginActivity2.UserId";

    EditText emailInput;
    EditText passwordInput;
    Button enter;

    long userId;
    private DBUserMovie database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        emailInput = (EditText) findViewById(R.id.login_email);
        passwordInput = (EditText) findViewById(R.id.login_password);
        enter = (Button) findViewById(R.id.btn_enter);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (Utils.hasLoginData(email, password)) {
                    database = DBUserMovie.getInstance(LoginActivity2.this);
                    findUser(email, password);
                } else {
                    Toast.makeText(LoginActivity2.this,
                            "Please, enter email and password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void findUser(String email, String password){
        AsyncTask findUser = new GetUserTask(userId, database, findUserInterface);
        findUser.execute(email);
    }


    public void intentListsActivity(long userId){

        Intent intent = new Intent(LoginActivity2.this, ListsActivity.class);
        intent.putExtra(EXTRA_ID, userId);
        startActivity(intent);
    }


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

            if (this.findUserInterface != null) {

                if (userId > 0) {
                    this.findUserInterface.userFound(userId);
                } else {
                    this.findUserInterface.userNotFound();
                }

            }

        }
    }
    GetUserTask.FindUser findUserInterface = new GetUserTask.FindUser() {
        @Override
        public void userFound(long id) {
            intentListsActivity(id);
        }

        @Override
        public void userNotFound() {
            String email2 = emailInput.getText().toString();
            String password2 = passwordInput.getText().toString();
            AsyncTask createUser = new CreateUserTask(userId, database, findUserInterface);
            createUser.execute(email2, password2);
        }
    };

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
            this.findUserInterface.userFound(userId);
        }
    }

}
