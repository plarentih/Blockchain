package com.example.plarent.blockchain.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.activity.StartingActivity;
import com.example.plarent.blockchain.tools.PermissionHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


public class PaymentFragment extends Fragment {

    private IntentIntegrator qrScan;

    public PaymentFragment() {
    }

    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        qrScan = new IntentIntegrator(getActivity());

        Button buttonPay = view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionHelper.checkForPermissions(getActivity())){
                    qrScan.initiateScan();
                }else {
                    PermissionHelper.askForPermissions(getActivity());
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Result is not found", Toast.LENGTH_LONG).show();
            }else {
                try{
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    Toast.makeText(getActivity(), jsonObject.getString("name"), Toast.LENGTH_LONG).show();
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
        actionBar.setTitle("Payment");
    }
}