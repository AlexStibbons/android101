package com.example.proto_korzo.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.proto_korzo.activities.fragments.AllMoviesFragment;
import com.example.proto_korzo.activities.fragments.FaveMoviesFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {

    // pageadapter should pass data from activity to fragments?

    private Fragment[] fragments;

    public ViewPageAdapter(FragmentManager fragmentManager){
        super(fragmentManager);

        fragments = new Fragment[] {
                new AllMoviesFragment(),
                new FaveMoviesFragment()
        };
    }

    @Override
    public Fragment getItem(int position) {
      return fragments[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "To watch";
            default:
                return "Tab";
        }

    }
}
