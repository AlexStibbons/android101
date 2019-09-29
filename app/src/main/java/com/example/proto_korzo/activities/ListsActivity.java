package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proto_korzo.R;
import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.User;

public class ListsActivity extends AppCompatActivity {

    private DBUserMovie database;
    TextView textId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = DBUserMovie.getInstance(this);

        Intent i = getIntent();
        Long id = i.getLongExtra(LoginActivity.EXTRA_ID, -1);

        // no communication with DB from main!
        User user = database.getUserDao().getUserById(id);

        textId = (TextView) findViewById(R.id.idView);
        textId.setText("This user's id is: " + id + " \nEmail: " + user.getEmail());

        // check network connectivity
        // get user data (using the passed on user id) from DB
        // populate faves list with user's faves
        // populate all list with random films
        //  add a star to corner of cards
        //  persist when clicked fave/unfave
        //  on list click (AdapterView / OnItemClickListener) open new activity
    }

   /* private static boolean hasNetwork() {
        if () {
            return true;
        }

        return false;
    }*/

}
