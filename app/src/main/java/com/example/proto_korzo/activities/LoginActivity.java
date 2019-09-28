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

    // declare variable that needs to be found and then passed to other activity
    long userId;

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

                String emailString = email.getText().toString();
                String passString = password.getText().toString();
                Log.d("getting info", "info " + emailString + " and " + passString);

                if (Utils.hasLoginData(emailString, passString)) { // *** password as String is not secure ***

                    // get DB instance
                    database = DBUserMovie.getInstance(LoginActivity.this);

                    // start background threads for communication with DB
                    // find or create user in DB
                    new RetreiveOrCreateUser(LoginActivity.this, userId)
                            .execute(emailString, passString);

                    // below logged before async task so id is 0
                    Log.d("after retretive", "userId should be not null, is " + userId);

                    // in any event, the above userId *should* now be valid

                    // does everything else run in onPostExecute now?

                    // pass userId to ListsActivity

                    // switch to ListsActivity

                    // again, logged before async task
                    Log.d(TAG, "data checked; true");
                    Log.d(TAG, "userId: " + userId);
                } else {
                    Toast.makeText(LoginActivity.this, "something happened", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "error during onClick/hasLoginData check");
                }
            }
        });

    }

    // AsyncTask --> only for short(er) tasks!

    private static class RetreiveOrCreateUser extends AsyncTask<String, Void, Long> {

        private WeakReference<LoginActivity> activityReference;
        private long userId;

        // pass in the userId from main!
        RetreiveOrCreateUser(LoginActivity context, long userId) {
            activityReference = new WeakReference<>(context);
            this.userId = userId;
        }

        // get from database
        @Override
        protected Long doInBackground(String... strings) {
            userId = activityReference.get().database.getUserDao().getUserIdByEmail(strings[0]);
            Log.d("Finding by email", "found: " + userId);
            if (userId <= 0 ) {
                userId = activityReference.get().database.getUserDao().addUser(new User(strings[0], strings[1]));
                return userId;
            }
            Log.d("from bkg", "userId is now: " + userId);
            return userId;
        }

        // doInBackground returns to onPostExecute, passes its return (which is also the userId from main)
        // onPostExecute runs in main thread

        // does the userId from activity really change?
        // should main from above be ignored because onPostExecute is the 'new' main?
        @Override
        protected void onPostExecute(Long userId) {
            super.onPostExecute(userId);
            Log.d("Async/passed to onPost", "id: " + userId);
            // if userId > 0
            // do something

        }
    }

}

