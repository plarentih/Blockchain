package com.example.plarent.blockchain.tools;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import static com.example.plarent.blockchain.activity.StartingActivity.BASE64_SAFE_URL_FLAGS;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.nio.charset.StandardCharsets.UTF_8;




/**
 * Created by plarent on 29/05/2018.
 */

public class EncryptionHelper {



    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    public static byte[] signData(String jsonData, byte[]seed) throws Exception {
        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
        signature.initSign(generateKeyPair().getPrivate());
        byte[] bytes =  jsonData.getBytes(UTF_8);
        signature.update(bytes);
        return bytes;
    }

    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedPv = Base64.decode(key64, Base64.DEFAULT);
        PKCS8EncodedKeySpec specPrivate = new PKCS8EncodedKeySpec(encodedPv);
        PrivateKey privateKey = keyFactory.generatePrivate(specPrivate);
        return privateKey;
    }

    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedPb = Base64.decode(stored, Base64.DEFAULT);
        X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
        PublicKey publicKeyString = keyFactory.generatePublic(keySpecPb);
        return publicKeyString;
    }

    public static String savePrivateKey(PrivateKey priv) throws GeneralSecurityException, UnsupportedEncodingException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        byte[] privateString = Base64.encode(priv.getEncoded(), Base64.DEFAULT);
        String str = new String(privateString, "UTF-8");
        return str;
    }


//    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
//        KeyFactory fact = KeyFactory.getInstance("RSA");
//        X509EncodedKeySpec spec = fact.getKeySpec(publ, X509EncodedKeySpec.class);
//        return base64Encode(spec.getEncoded());
//    }
}
