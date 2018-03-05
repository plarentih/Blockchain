package com.example.plarent.blockchain.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plarent.blockchain.R;

/**
 * Created by plarent on 05/03/2018.
 */

public class StartTwoFragment extends Fragment{

    public StartTwoFragment(){}

    public static StartTwoFragment newInstance(){
        StartTwoFragment starttwoFragment = new StartTwoFragment();
        return starttwoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_two, container,false);

        return view;
    }
}
