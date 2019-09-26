package com.example.proto_korzo.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proto_korzo.R;

public class ListsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_list);

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
