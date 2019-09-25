package com.example.proto_korzo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proto_korzo.R;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create scaffold
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        // populate layout? or initialize necessary variables?
        enter = (Button) findViewById(R.id.btn_enter);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);

        // why won't it accept a lambda expression here?
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Main/login activity", "clicking?");

                // if both email and pass are not null
                // *** password as String in not secure ***
                if (hasData(email.getText().toString(), password.getText().toString())) { // will a static work like this?
                    // persist data
                    // pass user id to list activity
                    // enter list activity
                    Log.d("Main/login activity", "data checked; true");
                } else {
                    // error
                    Log.e("Main/login activity", "error during onClick/hasData check");
                }
            }
        });

    }
    private static boolean hasData(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            return true;
        }
            return false;
    }
}
