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
    }

    // get all/some/a limited number of films & list them up
    // add a star to corner of card - persist fave/unfave
    // on list click (AdapterView / OnItemClickListener) open new activity
}
