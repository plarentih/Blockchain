package com.example.plarent.blockchain.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plarent.blockchain.R;

/**
 * Created by Plarent on 3/11/2018.
 */

public class StartThreeFragment extends Fragment {

    public StartThreeFragment(){}

    public static StartThreeFragment newInstance(){
        StartThreeFragment startThreeFragment = new StartThreeFragment();
        return startThreeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_three, container,false);

        return view;
    }
}
