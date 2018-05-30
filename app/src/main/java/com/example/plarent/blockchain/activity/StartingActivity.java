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

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.fragment.AccountFragment;
import com.example.plarent.blockchain.fragment.PaymentFragment;
import com.example.plarent.blockchain.fragment.TransferFragment;
import com.example.plarent.blockchain.fragment.WalletFragment;
import com.example.plarent.blockchain.tools.BottomNavigationViewHelper;
import com.example.plarent.blockchain.tools.EncryptionHelper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.libsodium.jni.SodiumConstants;
import org.libsodium.jni.crypto.Random;
import org.libsodium.jni.SodiumConstants;
import org.libsodium.jni.crypto.Random;
import org.libsodium.jni.keys.SigningKey;
import org.libsodium.jni.keys.VerifyKey;
import org.libsodium.jni.keys.KeyPair;


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
            byte[] seed = new Random().randomBytes(SodiumConstants.SECRETKEY_BYTES);
            generateEncryptionKeyPair(seed);
            generateSigningKeyPair(seed);
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
        KeyPair encryptionKeyPair = new KeyPair(seed);
        byte[] encryptionPublicKey = encryptionKeyPair.getPublicKey().toBytes();
        byte[] encryptionPrivateKey = encryptionKeyPair.getPrivateKey().toBytes();
        public_k = Base64.encodeToString(encryptionPublicKey, BASE64_SAFE_URL_FLAGS);
        private_k = Base64.encodeToString(encryptionPrivateKey, BASE64_SAFE_URL_FLAGS);
    }

    private void generateSigningKeyPair(byte[] seed) {
        SigningKey signingKey = new SigningKey(seed);
        VerifyKey verifyKey = signingKey.getVerifyKey();
        byte[] verifyKeyArray = verifyKey.toBytes();
        byte[] signingKeyArray = signingKey.toBytes();
        //signKeyView.setText(Base64.encodeToString(verifyKeyArray, BASE64_SAFE_URL_FLAGS));
        signature_k =  Base64.encodeToString(signingKeyArray, BASE64_SAFE_URL_FLAGS);
    }

    private void generateWallet() throws Exception {
        //public_k = EncryptionHelper.generateKeyPair().getPublic().toString();
        String url = "http://poc.serval.uni.lu:8080/api/services/cryptocurrency/v1/wallets";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pub_key", public_k);
        jsonObject.put("name", "Plarent");

        JSONObject outJson = new JSONObject();
        outJson.put("body", jsonObject);
        outJson.put("network_id", 0);
        outJson.put("protocol_version", 0);
        outJson.put("service_id", 1);
        outJson.put("message_id", 0);
        outJson.put("signature", signature_k);

        try {
            startProcess(url, outJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String startProcess(String url, String json) throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON_FORMAT, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        String res = response.body().string();
        boolean isSucc = response.isSuccessful();
        JSONObject dataReceived = new JSONObject(res);
        return dataReceived.toString();
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
