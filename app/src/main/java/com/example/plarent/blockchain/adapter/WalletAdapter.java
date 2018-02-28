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
import com.example.plarent.blockchain.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plarent on 28/02/2018.
 */

public class WalletAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> newContactList = new ArrayList<>();

    private static class ViewHolder{
        TextView textViewValue;
        TextView textViewCurrency;
    }

    public WalletAdapter(Context context, List<Contact> contactList) {
        super(context, 0, contactList);
        newContactList.addAll(contactList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contact contact = newContactList.get(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wallet_row, parent, false);
            viewHolder.textViewValue = convertView.findViewById(R.id.amountTextView);
            viewHolder.textViewCurrency = convertView.findViewById(R.id.currencyTextView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewValue.setText(String.valueOf(contact.getAmount()));
        viewHolder.textViewCurrency.setText(contact.getCurrency());
        return convertView;
    }
}
