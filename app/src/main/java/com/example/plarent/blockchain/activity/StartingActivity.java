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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.fragment.AccountFragment;
import com.example.plarent.blockchain.fragment.PaymentFragment;
import com.example.plarent.blockchain.fragment.TransferFragment;
import com.example.plarent.blockchain.fragment.WalletFragment;
import com.example.plarent.blockchain.tools.BottomNavigationViewHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.libsodium.jni.SodiumConstants;
import org.libsodium.jni.crypto.Random;
import org.libsodium.jni.keys.KeyPair;
import org.libsodium.jni.keys.SigningKey;
import org.libsodium.jni.keys.VerifyKey;


public class StartingActivity extends AppCompatActivity {

    public static final int BASE64_SAFE_URL_FLAGS = Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP;
    public static final MediaType JSON_FORMAT = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private SharedPreferences preferences;
    private String public_k;
    private String private_k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        centerTitle();

//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(30, TimeUnit.SECONDS);
//        builder.readTimeout(30, TimeUnit.SECONDS);
//        builder.writeTimeout(30, TimeUnit.SECONDS);
//        client = builder.build();

        preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        public_k = preferences.getString("PUBLIC_KEY", null);
        private_k = preferences.getString("PRIVATE_KEY", null);

        final String url = "http://poc.serval.uni.lu:8080/api/services/cryptocurrency/v1/wallet/6ce29b2d3ecadc434107ce52c287001c968a1b6eca3e5a1eb62a2419e2924b85";

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
            byte[] seed = new Random().randomBytes(SodiumConstants.SECRETKEY_BYTES);
            generateEncryptionKeyPair(seed);

            Thread threadi = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        generateWallet();
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

    private void generateEncryptionKeyPair(byte[] seed) {
        SigningKey signingKey = new SigningKey(seed);
        VerifyKey verifyKey = signingKey.getVerifyKey();
        byte[] verifyKeyArray = verifyKey.toBytes();
        byte[] signingKeyArray = signingKey.toBytes();

        KeyPair encryptionKeyPair = new KeyPair(seed);
        byte[] encryptionPublicKey = encryptionKeyPair.getPublicKey().toBytes();
        byte[] encryptionPrivateKey = encryptionKeyPair.getPrivateKey().toBytes();

        public_k = Base64.encodeToString(verifyKeyArray, BASE64_SAFE_URL_FLAGS);
        private_k = Base64.encodeToString(signingKeyArray, BASE64_SAFE_URL_FLAGS);
        int a = 0;
    }

    private void generateWallet() throws JSONException {
        String public_key = "6ce29b2d3ecadc434107ce52c287001c968a1b6eca3e5a1eb62a2419e2924b85";
        String private_key = "9f684227f1de663775848b3db656bca685e085391e2b00b0e115679fd45443ef58a5abeb555ab3d5f7a3cd27955a2079e5fd486743f36515c8e5bea07992100b";
        String url = "http://poc.serval.uni.lu:8080/api/services/cryptocurrency/v1/wallets";
        String jsonFile =
                "{ \"body\": {\"pub_key\":\"" + public_k+ "\",\"name\": \"Plarent\" }, \"network_id\": 0, \"protocol_version\": 0, \"service_id\": 1, \"message_id\": 0, \"signature\": \""+private_k+"\"}";
        JSONObject jsonObject = new JSONObject(jsonFile);
        try {
            startProcess(url, jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String startProcess(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON_FORMAT, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        return response.body().string();
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
