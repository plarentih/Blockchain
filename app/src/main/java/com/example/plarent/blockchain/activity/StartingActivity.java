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
import android.view.ViewGroup;;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.fragment.AccountFragment;
import com.example.plarent.blockchain.fragment.PaymentFragment;
import com.example.plarent.blockchain.fragment.TransferFragment;
import com.example.plarent.blockchain.fragment.WalletFragment;
import com.example.plarent.blockchain.tools.BottomNavigationViewHelper;

import java.io.IOException;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.SecureRandom;
//import java.security.Security;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//import org.apache.commons.codec.binary.Hex;

//import org.libsodium.jni.encoders.Encoder;
import org.abstractj.kalium.encoders.Encoder;

import static org.abstractj.kalium.NaCl.Sodium.CRYPTO_SIGN_ED25519_BYTES;
import static org.abstractj.kalium.NaCl.sodium;

import jnr.ffi.byref.LongLongByReference;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.abstractj.kalium.encoders.Hex;
import org.abstractj.kalium.keys.KeyPair;
import org.abstractj.kalium.keys.PrivateKey;
import org.abstractj.kalium.keys.PublicKey;
import org.abstractj.kalium.keys.SigningKey;
import org.json.JSONException;

//import org.libsodium.jni.SodiumConstants;
//import org.libsodium.jni.crypto.Random;
//import org.libsodium.jni.encoders.Hex;
//import org.libsodium.jni.keys.KeyPair;
//import org.libsodium.jni.keys.PrivateKey;
//import org.libsodium.jni.keys.PublicKey;
//import org.libsodium.jni.keys.SigningKey;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

public class StartingActivity extends AppCompatActivity {

    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    //public static final int AA = SodiumConstants.SECRETKEY_BYTES;
    Charset CHAR = Charset.forName("UTF-8");

    public static final int BASE64_SAFE_URL_FLAGS = Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP;
    public static final MediaType JSON_FORMAT = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private SharedPreferences preferences;
    private String public_k;
    private String private_k;
    private String signature_k;
    private byte [] sig;

    private Encoder encoder;
    private PrivateKey privateKey;
    private PublicKey publicKey;

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
            Thread threadi = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        //byte[] seed = new Random().randomBytes(SodiumConstants.SECRETKEY_BYTES);
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

    private void generateWallet() throws Exception {
//        KeyPair pair = EncryptionHelper.generateKeyPair();
//        PublicKey public_kk = pair.getPublic();
//        PrivateKey private_kk = pair.getPrivate();
//
//        byte[] pubBytes = Base64.encode(public_kk.getEncoded(), Base64.DEFAULT);
//        public_k = new String(pubBytes);
//
//        byte[] privBytes = Base64.encode(private_kk.getEncoded(), Base64.DEFAULT);
//        private_k = new String(privBytes);
        ////
//        KeyPair encryptionKeyPair = new KeyPair();
//        byte[] encryptionPublicKey = encryptionKeyPair.getPublicKey().toBytes();
//        byte[] encryptionPrivateKey = encryptionKeyPair.getPrivateKey().toBytes();
//        privateKey = encryptionKeyPair.getPrivateKey();
//        publicKey = encryptionKeyPair.getPublicKey();

//        SigningKey signingKey = new SigningKey();
//
//        public_k = publicKey.toString();
//        private_k = privateKey.toString();


        ////
        String url = "http://poc.serval.uni.lu:8080/api/services/cryptocurrency/v1/wallets";

        String json = "{  \n" +
                "   \"body\":{  \n" +
                "      \"pub_key\": \"" + "00" + "\",\n" +
                "      \"name\": \"Plarent\"\n" +
                "   },\n" +
                "   \"network_id\": 0,\n" +
                "   \"protocol_version\": 0,\n" +
                "   \"service_id\": 1,\n" +
                "   \"message_id\": 0\n" +
                "}";

//        int length = json.length();
//
//        //SigningKey sKey = new SigningKey(private_k, HEX);
//        String test = toHex(json);
//        encoder = new Hex();
//        //Hex hex = new Hex();
//        sig = sign(encoder.decode(test));
//        //sig = signingKey.sign(decode(test));
//        signature_k = encoder.encode(sig);


        //signature_k = EncryptionHelper.sign(json, privateKey);
        //boolean isSignatureCorrect = EncryptionHelper.verify(json, signature_k, public_kk);

        String endResult = "{  \n" +
                "   \"body\":{  \n" +
                "      \"pub_key\": \"" + "00" + "\",\n" +
                "      \"name\": \"Plarent\"\n" +
                "   },\n" +
                "   \"network_id\": 0,\n" +
                "   \"protocol_version\": 0,\n" +
                "   \"service_id\": 1,\n" +
                "   \"message_id\": 0,\n" +
                "   \"signature\": " + "\"" + "11" + "\"" + "\n" +
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

    //RAW
//    public byte[] decode(String data) {
//        return data != null?data.getBytes(CHAR):null;
//    }
//
//    public String encode(byte[] data) {
//        return data != null?new String(data, CHAR):null;
//    }


    //HEX
    private static byte[] decodeHex(final char[] data) {
        final int len = (data == null) ? 0 : data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }
        final byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    public byte[] decode(final String value) {
        char[] data = value != null ? value.toCharArray() : new char[0];
        return decodeHex(data);
    }

    private static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    private static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }


    public String encode(final byte[] data) {
        return new String(encodeHex(data));
    }

    private static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

///////////////////////
    public byte[] sign(byte[] message) {
        byte[] signature = new byte[CRYPTO_SIGN_ED25519_BYTES];
        LongLongByReference bufferLen = new LongLongByReference(0);
        sodium().crypto_sign_ed25519_detached(signature, bufferLen, message, message.length, privateKey.toBytes());
        return signature;
    }

    public String sign(String message, Encoder encoder) {
        byte[] signature = sign(encoder.decode(message));
        return encoder.encode(signature);
    }

    public static byte[] hexToByteArray(String hex) {
        hex = hex.length()%2 != 0?"0"+hex:hex;

        byte[] b = new byte[hex.length() / 2];

        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    public static String byteArrayToHexString(byte[] b) {
        int len = b.length;
        String data = new String();

        for (int i = 0; i < len; i++){
            data += Integer.toHexString((b[i] >>> 4) & 0xf);
            data += Integer.toHexString(b[i] & 0xf);
        }
        return data;
    }

    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }

    private static void addCharEntity(Integer aIdx, StringBuilder aBuilder){
        String padding = "";
        if( aIdx <= 9 ){
            padding = "00";
        }
        else if( aIdx <= 99 ){
            padding = "0";
        }
        else {
            //no prefix
        }
        String number = padding + aIdx.toString();
        aBuilder.append("&#" + number + ";");
    }

    public static String forHTML(String aText){
        final StringBuilder result = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(aText);
        char character =  iterator.current();
        while (character != CharacterIterator.DONE ){
            if (character == '<') {
                result.append("&lt;");
            }
            else if (character == '>') {
                result.append("&gt;");
            }
            else if (character == '&') {
                result.append("&amp;");
            }
            else if (character == '\"') {
                result.append("&quot;");
            }
            else if (character == '\t') {
                addCharEntity(9, result);
            }
            else if (character == '!') {
                addCharEntity(33, result);
            }
            else if (character == '#') {
                addCharEntity(35, result);
            }
            else if (character == '$') {
                addCharEntity(36, result);
            }
            else if (character == '%') {
                addCharEntity(37, result);
            }
            else if (character == '\'') {
                addCharEntity(39, result);
            }
            else if (character == '(') {
                addCharEntity(40, result);
            }
            else if (character == ')') {
                addCharEntity(41, result);
            }
            else if (character == '*') {
                addCharEntity(42, result);
            }
            else if (character == '+') {
                addCharEntity(43, result);
            }
            else if (character == ',') {
                addCharEntity(44, result);
            }
            else if (character == '-') {
                addCharEntity(45, result);
            }
            else if (character == '.') {
                addCharEntity(46, result);
            }
            else if (character == '/') {
                addCharEntity(47, result);
            }
            else if (character == ':') {
                addCharEntity(58, result);
            }
            else if (character == ';') {
                addCharEntity(59, result);
            }
            else if (character == '=') {
                addCharEntity(61, result);
            }
            else if (character == '?') {
                addCharEntity(63, result);
            }
            else if (character == '@') {
                addCharEntity(64, result);
            }
            else if (character == '[') {
                addCharEntity(91, result);
            }
            else if (character == '\\') {
                addCharEntity(92, result);
            }
            else if (character == ']') {
                addCharEntity(93, result);
            }
            else if (character == '^') {
                addCharEntity(94, result);
            }
            else if (character == '_') {
                addCharEntity(95, result);
            }
            else if (character == '`') {
                addCharEntity(96, result);
            }
            else if (character == '{') {
                addCharEntity(123, result);
            }
            else if (character == '|') {
                addCharEntity(124, result);
            }
            else if (character == '}') {
                addCharEntity(125, result);
            }
            else if (character == '~') {
                addCharEntity(126, result);
            }
            else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }
}
