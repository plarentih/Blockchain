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

public class WalletWithdrawalsFragment extends Fragment {

    private ListView listView;
    private DepositWithdrawalWalletAdapter adapter;
    private List<Transaction> transactionList;

    public WalletWithdrawalsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        transactionList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_withdrawals, container, false);
        listView = view.findViewById(R.id.wallet_listView);
        transactionList = dummyData();
        adapter = new DepositWithdrawalWalletAdapter(getContext(), transactionList);
        listView.setAdapter(adapter);
        return view;
    }

    private List<Transaction> dummyData(){
        List<Transaction> transactions = new ArrayList<>();
        Transaction t1 = new Transaction("1", "nskvsld", 12, "EUR", "09.12.2017");
        Transaction t2 = new Transaction("2", "j423oij", 433, "EUR", "02.03.2012");
        Transaction t3 = new Transaction("3", "fsdl343", 123, "EUR", "17.03.2017");
        Transaction t4 = new Transaction("4", "knj3n43", 98, "EUR", "21.09.2013");
        Transaction t5 = new Transaction("5", "923k4j3", 1232, "EUR", "09.09.2009");
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);
        transactions.add(t5);
        return transactions;
    }
}
