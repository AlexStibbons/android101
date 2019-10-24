package com.example.proto_korzo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.proto_korzo.R;
import com.example.proto_korzo.adapters.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

// 1. check network connectivity
// 2. get user data (using the passed on user id) from DB
// 3. populate faves list with user's faves
// 4. populate all list with random films
// 5. persist when clicked fave/unfave
// 6. start new activity when clicked on movie

public class ListsActivity extends AppCompatActivity {

    private static final String TAG = ListsActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lists);

        Intent i = getIntent();
        int id = i.getIntExtra(LoginActivity.EXTRA_ID, -1);
        Log.e(TAG, "ACTIVITY GOT ID: " + id);

        // VIEW PAGER & TABLAYOUT FOR TABS + FRAGMENTS
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), id));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


    }

}
