package com.example.plarent.blockchain.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.plarent.blockchain.fragment.StartOneFragment;
import com.example.plarent.blockchain.fragment.StartTwoFragment;

/**
 * Created by plarent on 05/03/2018.
 */

public class SwipePagerAdapter extends FragmentPagerAdapter {

    public SwipePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return StartOneFragment.newInstance();
            case 1:
                return StartTwoFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
