package com.example.plarent.blockchain.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.plarent.blockchain.fragment.WalletDepositsFragment;
import com.example.plarent.blockchain.fragment.WalletWithdrawalsFragment;

/**
 * Created by plarent on 12/03/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                WalletDepositsFragment tab1 = new WalletDepositsFragment();
                return tab1;
            case 1:
                WalletWithdrawalsFragment tab2 = new WalletWithdrawalsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

