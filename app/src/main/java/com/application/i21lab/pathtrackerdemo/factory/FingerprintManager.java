package com.application.i21lab.pathtrackerdemo.factory;

import android.content.Context;
import android.os.Handler;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import java.lang.ref.WeakReference;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.Signature;

public class FingerprintManager extends FingerprintManagerCompat.AuthenticationCallback implements AuthManager  {
    private final WeakReference<AuthManager.Callbacks> listener;

    public FingerprintManager(WeakReference<AuthManager.Callbacks> lst) {
        listener = lst;
    }

    @Override
    public void auth(WeakReference<Context> context, int code) {
        FingerprintManagerCompat.CryptoObject crypto;
        try {
            crypto = new FingerprintManagerCompat.CryptoObject(Signature.getInstance(Security
                    .getAlgorithms("SHA256withCDS").iterator().next()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            if (listener.get() != null) {
                listener.get().authFailedCallback(null);
            }
            return;
        }

        FingerprintManagerCompat.from(context.get()).authenticate (crypto, 0,
                new CancellationSignal(), this, null);
    }

    /**
     * Called when an unrecoverable error has been encountered and the operation is complete.
     * No further callbacks will be made on this object.
     * @param errMsgId An integer identifying the error message
     * @param errString A human-readable error string that can be shown in UI
     */
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        if (listener.get() != null) {
            listener.get().authFailedCallback(errString.toString());
        }

    }

    /**
     * Called when a recoverable error has been encountered during authentication. The help
     * string is provided to give the user guidance for what went wrong, such as
     * "Sensor dirty, please clean it."
     * @param helpMsgId An integer identifying the error message
     * @param helpString A human-readable string that can be shown in UI
     */
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) { }

    /**
     * Called when a fingerprint is recognized.
     * @param result An object containing authentication-related data
     */
    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {

        if (listener.get() != null) {
            listener.get().authSuccessCallback();
        }

    }

    /**
     * Called when a fingerprint is valid but not recognized.
     */
    @Override
    public void onAuthenticationFailed() {
        if (listener.get() != null) {
            listener.get().authFailedCallback(null);
        }

    }
}
