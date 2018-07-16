package com.example.plarent.blockchain.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.activity.StartingActivity;
import com.example.plarent.blockchain.tools.PermissionHelper;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class PaymentFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private FrameLayout frameLayout;
    private ZXingScannerView scannerView;
    private Button buttonPay;
    private View view;

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
        view = inflater.inflate(R.layout.fragment_payment, container, false);
        frameLayout = view.findViewById(R.id.frame_layouti);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        frameLayout.getLayoutParams().width = height / 3;
        frameLayout.getLayoutParams().height = height / 3;
        buttonPay = view.findViewById(R.id.button_pay);
        setButtonClick();
        return view;
    }

    private void setButtonClick(){
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionHelper.checkForPermissions(getActivity())){
                    QRScanner(view);
                }else {
                    PermissionHelper.askForPermissions(getActivity());
                }
            }
        });
    }

    public void QRScanner(View view){
        buttonPay.setVisibility(View.GONE);
        if(scannerView == null){
            scannerView = new ZXingScannerView(getActivity());
            frameLayout.addView(scannerView);
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }else {
            frameLayout.setVisibility(View.VISIBLE);
            scannerView.startCamera();
            scannerView.setResultHandler(this);
        }
    }

    @Override
    public void handleResult(Result result) {
        String text = result.getText();
        Toast.makeText(getActivity(), "You have made a payment of " + text + " EUR", Toast.LENGTH_LONG).show();
        frameLayout.setVisibility(View.GONE);
        buttonPay.setVisibility(View.VISIBLE);
        int amountPayed = Integer.parseInt(text);
        TransferFragment.AMOUNT = 1900 - amountPayed;

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

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(scannerView != null){
                        scannerView.stopCamera();
                    }
                    frameLayout.setVisibility(View.GONE);
                    buttonPay.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(scannerView != null){
            scannerView.stopCamera();
        }
    }


}