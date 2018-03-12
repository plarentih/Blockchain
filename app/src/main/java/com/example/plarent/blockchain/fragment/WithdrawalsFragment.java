package com.example.plarent.blockchain.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plarent.blockchain.R;

/**
 * Created by plarent on 12/03/2018.
 */

public class WithdrawalsFragment extends Fragment {

    public WithdrawalsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdrawals, container, false);
        return view;
    }
}
