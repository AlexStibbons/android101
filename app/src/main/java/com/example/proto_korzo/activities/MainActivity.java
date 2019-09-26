package com.example.proto_korzo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proto_korzo.R;
import com.example.proto_korzo.Utils;

public class MainActivity extends AppCompatActivity {

    // a tag for logs
    private static final String TAG = MainActivity.class.getSimpleName();

    // declare necessary variables : long way
    EditText email;
    EditText password;
    Button enter;

    // initialize necessary variables : short way using ButterKnife
    // [no need to find them in onCreate]
    // @BindView(R.id.btn_enter) Button enter;

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

                String emailString = email.getText().toString();
                String passString = password.getText().toString();

                // if the fields are empty, will the below work?
                if (Utils.hasLoginData(emailString, passString)) {
                    // *** password as String is not secure ***

                    // find user in remote DB
                    //long userId = Utils.findUserIdByEmail(emailString);

                    // if user is not in DB, create one, return user id
                    //if (userId == -1 ) {
                    // create user
                    // User newUser = new User(emailString, passString);
                    // userId = newUser.getId();
                    // }

                    // pass userId to ListsActivity

                    // switch to ListsActivity

                    Log.d(TAG, "data checked; true");
                } else {
                    // error
                    // a pop maybe? "a toast"
                    Toast.makeText(MainActivity.this, "input fields", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "error during onClick/hasLoginData check"); // try/catch instead of if/else?
                }
            }
        });

    }

}
