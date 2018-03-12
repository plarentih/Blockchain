package com.example.plarent.blockchain.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.activity.HistoryWalletActivity;
import com.example.plarent.blockchain.activity.StartingActivity;
import com.example.plarent.blockchain.adapter.WalletAdapter;
import com.example.plarent.blockchain.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plarent on 19/02/2018.
 */

public class WalletFragment extends Fragment {

    private ListView listView;
    private WalletAdapter walletAdapter;
    private List<Contact> contacts;
    private Button historyBtn;

    public WalletFragment(){
    }

    public static WalletFragment newInstance() {
        WalletFragment fragment = new WalletFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contacts = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragement_wallet, container, false);
        historyBtn = view.findViewById(R.id.history_btn);
        listView = view.findViewById(R.id.wallet_listView);
        Contact contact = new Contact("1", "Plarent Haxhidauti", "661932412",
                1389, "EURO");
        contacts.add(contact);
        walletAdapter = new WalletAdapter(getContext(), contacts);
        listView.setAdapter(walletAdapter);

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HistoryWalletActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        StartingActivity activity = (StartingActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Wallet");
    }
}
