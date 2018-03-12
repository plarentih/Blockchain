package com.example.plarent.blockchain.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plarent on 12/03/2018.
 */

public class DepositWithdrawalWalletAdapter extends ArrayAdapter<Transaction> {

    private ArrayList<Transaction> newTransactions = new ArrayList<>();

    private static class ViewHolder{
        TextView referenceTxt;
        TextView amountTxt;
        TextView currencyTxt;
        TextView dateTxt;
    }

    public DepositWithdrawalWalletAdapter(Context context, List<Transaction> transactionList){
        super(context, 0, transactionList);
        newTransactions.addAll(transactionList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Transaction transaction = newTransactions.get(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.deposit_wallet_row, parent, false);
            viewHolder.referenceTxt = convertView.findViewById(R.id.referenceView);
            viewHolder.amountTxt = convertView.findViewById(R.id.amountView);
            viewHolder.currencyTxt = convertView.findViewById(R.id.currencyView);
            viewHolder.dateTxt = convertView.findViewById(R.id.dateView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.referenceTxt.setText(transaction.getReference());
        viewHolder.amountTxt.setText(String.valueOf(transaction.getAmount()));
        viewHolder.currencyTxt.setText(transaction.getCurrency());
        viewHolder.dateTxt.setText(transaction.getDate());
        return convertView;
    }
}
