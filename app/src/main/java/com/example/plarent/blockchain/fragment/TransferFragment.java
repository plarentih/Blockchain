package com.example.plarent.blockchain.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.activity.PeopleActivity;
import com.example.plarent.blockchain.activity.StartingActivity;
import com.example.plarent.blockchain.activity.TransferHistoryActivity;
import com.example.plarent.blockchain.model.Person;
import java.util.ArrayList;
import java.util.List;

public class TransferFragment extends Fragment {

    private Button sendButton, historyButton, peopleButton;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText amountTxt;
    private EditText amountSend;
    ArrayAdapter<String> adapter;

    List<Person> personList;
    List<String> personNames;

    public static int AMOUNT = 1900;

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
        autoCompleteTextView = view.findViewById(R.id.receiver_address);
        historyButton = view.findViewById(R.id.btnHistory);
        peopleButton = view.findViewById(R.id.btnPeople);
        sendButton = view.findViewById(R.id.buttonSend);
        amountTxt = view.findViewById(R.id.amount);
        amountSend = view.findViewById(R.id.amount_send);

        setAutoCompleteView();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to do this transfer? Please check the details carefully.");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String currentNoV = amountTxt.getText().toString();
                        int currentvalue = Integer.parseInt(currentNoV);

                        String currentNo = amountSend.getText().toString();
                        int value = Integer.parseInt(currentNo);


                        AMOUNT = currentvalue - value;
                        amountTxt.setText("" + AMOUNT);
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

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                amountTxt.setText("" + 100);
//                AMOUNT = 100;
//            }
//        }, 2000);
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
        setAutoCompleteView();

    }

    private static List<Person> getAllPersons(){
        return new Select()
                .from(Person.class)
                .execute();
    }

    private void setAutoCompleteView(){
        personList = getAllPersons();
        personNames = new ArrayList<>();
        for(Person person : personList){
            personNames.add(person.getName());
        }

        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, personNames);
        autoCompleteTextView.setAdapter(adapter);
    }
}
