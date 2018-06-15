package com.example.plarent.blockchain.activity;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.fragment.AccountFragment;
import com.example.plarent.blockchain.fragment.PaymentFragment;
import com.example.plarent.blockchain.fragment.TransferFragment;
import com.example.plarent.blockchain.fragment.WalletFragment;
import com.example.plarent.blockchain.tools.BottomNavigationViewHelper;
import com.example.plarent.blockchain.tools.EncryptionHelper;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;


public class StartingActivity extends AppCompatActivity {

    public static final int BASE64_SAFE_URL_FLAGS = Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP;
    public static final MediaType JSON_FORMAT = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private SharedPreferences preferences;
    private String public_k;
    private String private_k;
    private String signature_k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        centerTitle();

        preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        public_k = preferences.getString("PUBLIC_KEY", null);
        private_k = preferences.getString("PRIVATE_KEY", null);

        final String url = "http://poc.serval.uni.lu:8080/api/services/cryptocurrency/v1/wallets";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    runi(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_payment:
                                selectedFragment = PaymentFragment.newInstance();
                                break;
                            case R.id.action_transfers:
                                selectedFragment = TransferFragment.newInstance();
                                break;
                            case R.id.action_account:
                                selectedFragment = AccountFragment.newInstance();
                                break;
                            case R.id.action_wallet:
                                selectedFragment = WalletFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        getSupportFragmentManager().executePendingTransactions();
                        return true;
                    }
                });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PaymentFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

        if(public_k.trim().isEmpty() && private_k.trim().isEmpty()){
            //byte[] seed = new Random().randomBytes(SodiumConstants.SECRETKEY_BYTES);
            Thread threadi = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        generateWallet();
                        //testProcess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threadi.start();


            editor.putString("PUBLIC_KEY", public_k);
            editor.putString("PRIVATE_KEY", private_k);
            editor.commit();
        }
    }

    private void generateWallet() throws Exception {
        KeyPair pair = EncryptionHelper.generateKeyPair();
        PublicKey public_kk = pair.getPublic();
        PrivateKey private_kk = pair.getPrivate();

        byte[] pubBytes = Base64.encode(public_kk.getEncoded(), Base64.DEFAULT);
        public_k = new String(pubBytes);

        

        byte[] privBytes = Base64.encode(private_kk.getEncoded(), Base64.DEFAULT);
        private_k = new String(privBytes);


        String url = "http://poc.serval.uni.lu:8080/api/services/cryptocurrency/v1/wallets";

        String json = "{  \n" +
                "   \"body\":{  \n" +
                "      \"pub_key\": \"" + public_k + "\",\n" +
                "      \"name\": \"Plarent\"\n" +
                "   },\n" +
                "   \"network_id\": 0,\n" +
                "   \"protocol_version\": 0,\n" +
                "   \"service_id\": 1,\n" +
                "   \"message_id\": 0\n" +
                "}";

        signature_k = EncryptionHelper.sign(json, private_kk);
        boolean isSignatureCorrect = EncryptionHelper.verify(json, signature_k, public_kk);

        String endResult = "{  \n" +
                "   \"body\":{  \n" +
                "      \"pub_key\": \"" + public_k + "\",\n" +
                "      \"name\": \"Plarent\"\n" +
                "   },\n" +
                "   \"network_id\": 0,\n" +
                "   \"protocol_version\": 0,\n" +
                "   \"service_id\": 1,\n" +
                "   \"message_id\": 0,\n" +
                "   \"signature\":" + "\"" + signature_k + "\"" + "\n" +
                "}";

        try {
            startProcess(url, endResult);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String startProcess(String url, String json) throws IOException, JSONException {
        try{
            RequestBody body = RequestBody.create(JSON_FORMAT, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = null;

            response = client.newCall(request).execute();
            Log.d("PLARENT!!!!!", response.toString());
            int code = response.code();
            String res = response.body().string();
            return res;
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public String runi(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        int code = response.code();
        return response.body().string();
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
