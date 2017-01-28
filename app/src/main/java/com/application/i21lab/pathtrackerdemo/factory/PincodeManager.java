package com.application.i21lab.pathtrackerdemo.factory;

import android.content.Context;

import java.lang.ref.WeakReference;

public class PincodeManager implements AuthManager {
    private final WeakReference<Callbacks> listener;
    private final static int AUTH_CODE = 1234;

    public PincodeManager(WeakReference<AuthManager.Callbacks> lst) {
        listener = lst;
    }
    @Override
    public void auth(WeakReference<Context> context, int code) {
        if (code != -1 &&
                code == AUTH_CODE) {
            if (listener.get() != null) {
                listener.get().authSuccessCallback();
                return;
            }
        }

        if (listener.get() != null)
            listener.get().authFailedCallback(null);
    }
}
