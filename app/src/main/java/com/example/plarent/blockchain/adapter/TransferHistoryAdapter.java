package com.example.plarent.blockchain.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.model.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plarent on 12/03/2018.
 */

public class TransferHistoryAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> newContactList = new ArrayList<>();

    private static class ViewHolder{
        TextView nameTxt;
        TextView amountTxt;
        TextView currencyTxt;
        TextView walletTxt;
        TextView fromTo;
    }

    public TransferHistoryAdapter(Context context, List<Contact> transactionList){
        super(context, 0, transactionList);
        newContactList.addAll(transactionList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contact contact = newContactList.get(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transfer_history_row, parent, false);
            viewHolder.nameTxt = convertView.findViewById(R.id.nameContact);
            viewHolder.amountTxt = convertView.findViewById(R.id.amountHistoryTxt);
            viewHolder.currencyTxt = convertView.findViewById(R.id.currencyTxt);
            viewHolder.walletTxt = convertView.findViewById(R.id.walletAddressTxt);
            viewHolder.fromTo = convertView.findViewById(R.id.txtView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(contact.getAmount() < 500){
            viewHolder.fromTo.setText("To - ");
            viewHolder.fromTo.setTextColor(getContext().getResources().getColor(R.color.red));
        }
        viewHolder.nameTxt.setText(contact.getContactName());
        viewHolder.amountTxt.setText(String.valueOf(contact.getAmount()));
        viewHolder.currencyTxt.setText(contact.getCurrency());
        viewHolder.walletTxt.setText(contact.getContactNumber());
        return convertView;
    }
}
