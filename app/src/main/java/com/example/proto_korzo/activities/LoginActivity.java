package com.example.proto_korzo.activities;

import android.content.Intent;
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
import com.example.proto_korzo.asyncTasks.AsyncTaskManager;
import com.example.proto_korzo.database.DBUserMovie;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static final String EXTRA_ID = "com.example.proto_korzo.LoginActivity.UserId";

    EditText etEmail;
    EditText etPassword;
    Button enter;
    private DBUserMovie database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_login);

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

    public void fetchUser(final String email, final String password) {

        AsyncTaskManager.findUser(database, email, new AsyncTaskManager.OnFindUser() {
            @Override
            public void onUserFound(int userId) {
                Log.e(TAG, "INTERFACE USER ID IS " + userId);
                changeActivity(userId);
            }

            @Override
            public void onUserNotFound() {

            AsyncTaskManager.createUser(database, email, password, new AsyncTaskManager.OnFindUser() {
                @Override
                public void onUserFound(int userId) {
                    changeActivity(userId);
                }

                @Override
                public void onUserNotFound() {
                    Log.e(TAG, "onUserNotFound: error when creating");
                }
            });

            }
        });
    }

    public void changeActivity(int userId) {

        Log.e(TAG, "CHANGE ACT USER ID " + userId);

        Intent intent = new Intent(LoginActivity.this, ListsActivity.class);
        intent.putExtra(EXTRA_ID, userId);
        startActivity(intent);
        //finish();
    }

}

