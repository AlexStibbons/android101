package com.example.proto_korzo.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proto_korzo.R;
import com.example.proto_korzo.Utils;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.User;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {

    // a tag for logs
    private static final String TAG = LoginActivity.class.getSimpleName();

    // initialize necessary variables : short way using ButterKnife
    // [no need to find them in onCreate]
    // @BindView(R.id.btn_enter) Button enter;

    // declare necessary variables : long way
    EditText email; // --> EditText OR TextInputEditText ?
    EditText password;
    Button enter;

    // where should userId go?
    long userId; // --> how to set this value from AsyncTask?

    // declare database
    private DBUserMovie database;

    @Override // should Bundle be @Nullable?
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        // initialize necessary variables : long way
        enter = (Button) findViewById(R.id.btn_enter);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicking");

                // if the fields are empty, will the below work?
                String emailString = email.getText().toString();
                String passString = password.getText().toString();

                if (Utils.hasLoginData(emailString, passString)) { // --> then perform checks in DB + return ID
                    // *** password as String is not secure ***

                    // get DB instance
                    database = DBUserMovie.getInstance(LoginActivity.this);

                    // start background threads for communication with DB
                    // find user in DB
                    new RetreiveUser(LoginActivity.this).execute(emailString);

                    // if user is found, the above userId is now > 0
                    // if not, the user must be created
                    if (userId <= 0) {
                        User newUser = new User(emailString, passString);
                        Log.d("create user if id 0", "user" + newUser.getEmail() + " and " + newUser.getPassword());
                        new InsertUser(LoginActivity.this).execute(newUser);
                    }
                    // in any event, the above userId is now valid

                    // pass userId to ListsActivity

                    // switch to ListsActivity

                    Log.d(TAG, "data checked; true");
                    Log.d(TAG, "userId: " + userId);
                } else {
                    // error
                    // a pop maybe? "a toast"
                    Toast.makeText(LoginActivity.this, "something happened", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "error during onClick/hasLoginData check"); // try/catch instead of if/else?
                }
            }
        });

    }

    // AsyncTask --> only for short(er) tasks!

    // inner class for FETCHING existing user
    private static class RetreiveUser extends AsyncTask<String, Void, Long> {

        private WeakReference<LoginActivity> activityReference;

        RetreiveUser(LoginActivity context) {
            activityReference = new WeakReference<>(context);
        }

        // get from database
        @Override
        protected Long doInBackground(String... strings) {
            long userId = activityReference.get().database.getUserDao().getUserIdByEmail(strings[0]);
            return userId;
        }

        // doInBackground returns here - userId is found userId from above
        @Override
        protected void onPostExecute(Long userId) {
            super.onPostExecute(userId);
            Log.d("Find / onPost", "id: " + userId);
            long userId_activity = activityReference.get().userId;
            if (userId > 0) {
                userId_activity = userId;
            } else {
                userId_activity = -1;
            }
        }
    }

    // inner class for CREATING new user
    private static class InsertUser extends AsyncTask<User, Void, Long> {


        private WeakReference<LoginActivity> activityReference;

        InsertUser(LoginActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Long doInBackground(User... users) {

            long userId = activityReference.get().database.getUserDao().addUser(users[0]);
            Log.d("bkg / add", "id: " + userId);
            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            super.onPostExecute(userId);
            long activity_userId = activityReference.get().userId;
            Log.d("Insert / onPost", "id: " + userId);
            activity_userId = userId;
        }

    }

}

