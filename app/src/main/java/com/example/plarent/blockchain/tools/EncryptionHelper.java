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
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(256, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        byte[] encodedPv = Base64.decode(key64, Base64.DEFAULT);
        PKCS8EncodedKeySpec specPrivate = new PKCS8EncodedKeySpec(encodedPv);
        PrivateKey privateKey = keyFactory.generatePrivate(specPrivate);
        return privateKey;
    }

    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        byte[] encodedPb = Base64.decode(stored, Base64.DEFAULT);
        X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
        PublicKey publicKeyString = keyFactory.generatePublic(keySpecPb);
        return publicKeyString;
    }

    public static String savePrivateKey(PrivateKey priv) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] keyBytes = priv.getEncoded();
        String ss = Base64.encodeToString(keyBytes, Base64.DEFAULT);
        return ss;
    }


    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
        byte[] keyBytes = publ.getEncoded();
        String ss = Base64.encodeToString(keyBytes, Base64.DEFAULT);
        return ss;
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA1withECDSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.encodeToString(signature, Base64.DEFAULT);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA1withECDSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = Base64.decode(signature, Base64.DEFAULT);

        return publicSignature.verify(signatureBytes);
    }
}
