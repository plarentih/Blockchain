package com.example.plarent.blockchain.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.model.Person;
import com.example.plarent.blockchain.tools.PermissionHelper;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PeopleActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Button scanBtn, addBtn;
    private ZXingScannerView scannerView;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private Person person;
    private EditText numberedit, walletedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        centerTitle();
        scanBtn = findViewById(R.id.button3);

        person = new Person();

        addBtn = findViewById(R.id.button4);
        linearLayout = findViewById(R.id.ori);
        relativeLayout = findViewById(R.id.infoo);
        numberedit = findViewById(R.id.receiver_address);
        walletedit = findViewById(R.id.wallet);

        frameLayout = findViewById(R.id.frame_layout);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        frameLayout.getLayoutParams().width = height / 3;
        frameLayout.getLayoutParams().height = height / 3;

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionHelper.checkForPermissions(PeopleActivity.this)){
                    QRScanner(view);
                }else {
                    PermissionHelper.askForPermissions(PeopleActivity.this);
                }

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable number = numberedit.getText();
                Editable wall = walletedit.getText();
                String no = number.toString().trim();
                String address = wall.toString().trim();
                Person person = new Person();
                person.phoneNumber = no;
                person.wallet = address;
                person.save();
                finish();
            }
        });
    }

    public void QRScanner(View view){
        addBtn.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
        if(scannerView == null){
            scannerView = new ZXingScannerView(this);
            frameLayout.addView(scannerView);
            scannerView.setResultHandler( this);
            scannerView.startCamera();
        }else {
            frameLayout.setVisibility(View.VISIBLE);
            scannerView.startCamera();
            scannerView.setResultHandler( this);
        }
    }

    @Override
    public void handleResult(Result rawResult){
        String result = rawResult.getText();
        String[] results = result.split("\\s+");
        if(results.length > 2){
        }else {
            person.phoneNumber = results[0];
            person.wallet = results[1];
            person.save();
        }

        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        frameLayout.setVisibility(View.GONE);

        addBtn.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(scannerView != null){
            scannerView.stopCamera();
        }
    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }
}
