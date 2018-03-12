package com.example.plarent.blockchain.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.activity.PeopleActivity;
import com.example.plarent.blockchain.activity.StartingActivity;
import com.example.plarent.blockchain.activity.TransferHistoryActivity;
import com.google.zxing.integration.android.IntentIntegrator;

public class TransferFragment extends Fragment {

    private Button sendButton, historyButton, peopleButton;

    public TransferFragment() {

    }

    public static TransferFragment newInstance() {
        TransferFragment fragment = new TransferFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);

        historyButton = view.findViewById(R.id.btnHistory);
        peopleButton = view.findViewById(R.id.btnPeople);
        sendButton = view.findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to do this transfer? Please check the details carefully.");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setCancelable(true);
                builder.show();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TransferHistoryActivity.class);
                startActivity(intent);
            }
        });

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PeopleActivity.class);
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
        actionBar.setTitle("Transfers");
    }
}
