package com.example.proto_korzo.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.proto_korzo.fragments.AllMoviesFragment;
import com.example.proto_korzo.fragments.FaveMoviesFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public ViewPageAdapter(FragmentManager fragmentManager, int id){
        super(fragmentManager);

        fragments = new Fragment[] {
                AllMoviesFragment.getInstance(id),
                FaveMoviesFragment.getInstance(id)
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
                return "Movies";
            case 1:
                return "Watchlist";
            default:
                return "Tab";
        }

    }
}
