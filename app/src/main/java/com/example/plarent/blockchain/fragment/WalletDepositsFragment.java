package com.example.plarent.blockchain.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.adapter.DepositWithdrawalWalletAdapter;
import com.example.plarent.blockchain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plarent on 12/03/2018.
 */

public class WalletDepositsFragment extends Fragment {

    private ListView listView;
    private DepositWithdrawalWalletAdapter adapter;
    private List<Transaction> transactionList;


    public WalletDepositsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        transactionList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_deposits, container, false);
        listView = view.findViewById(R.id.wallet_listView);
        transactionList = dummyData();
        adapter = new DepositWithdrawalWalletAdapter(getContext(), transactionList);
        listView.setAdapter(adapter);
        return view;
    }

    private List<Transaction> dummyData(){
        List<Transaction> transactions = new ArrayList<>();
        Transaction t1 = new Transaction("1", "s39J8he", 213, "EUR", "02.03.2018");
        Transaction t2 = new Transaction("2", "fdsf5ef", 23, "EUR", "01.05.2016");
        Transaction t3 = new Transaction("3", "oadfjsf", 543, "EUR", "12.12.2014");
        Transaction t4 = new Transaction("4", "vknweoe", 12, "EUR", "23.05.2018");
        Transaction t5 = new Transaction("5", "pqowekd", 432, "EUR", "17.03.2017");
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);
        transactions.add(t5);
        return transactions;
    }
}
