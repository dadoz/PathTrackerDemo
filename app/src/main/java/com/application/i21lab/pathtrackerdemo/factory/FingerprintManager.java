package com.application.i21lab.pathtrackerdemo.factory;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.widget.Toast;

import com.application.i21lab.pathtrackerdemo.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.content.Context.KEYGUARD_SERVICE;

public class FingerprintManager extends FingerprintManagerCompat.AuthenticationCallback implements AuthManager  {
    private final WeakReference<AuthManager.Callbacks> listener;
    private final KeyguardManager keyguardManager;
    private Cipher cipher;
    private KeyStore keyStore;
    private static final String KEY_NAME = "pathtrackerdemo_key";

    public FingerprintManager(WeakReference<AuthManager.Callbacks> lst, Context context) {
        listener = lst;
        keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
    }

    @Override
    public void auth(WeakReference<Context> context, int code) {
        try {
//            Toast.makeText(context.get(), "hey auth", Toast.LENGTH_SHORT).show();
            initKeyGeneration();
            if (!initCipher()) {
                if (listener.get() != null) {
                    listener.get().authFailedCallback("failed to init cipher");
                }
                return;
            }

//            Toast.makeText(context.get(), "hey auth 1", Toast.LENGTH_SHORT).show();
            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            FingerprintManagerCompat.from(context.get()).authenticate (cryptoObject, 0,
                    new CancellationSignal(), this, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (listener.get() != null) {
                listener.get().authFailedCallback(e.getMessage());
            }
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        if (listener.get() != null) {
            listener.get().authFailedCallback(errString.toString());
        }

    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) { }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        if (listener.get() != null) {
            listener.get().authSuccessCallback();
        }

    }

    @Override
    public void onAuthenticationFailed() {
        if (listener.get() != null) {
            listener.get().authFailedCallback(null);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void initKeyGeneration() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);

            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException|
                CertificateException | IOException | KeyStoreException |
                NoSuchProviderException  e) {

        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException |
                NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
