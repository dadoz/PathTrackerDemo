package com.application.i21lab.pathtrackerdemo.factory;

import android.content.Context;

import java.lang.ref.WeakReference;

public interface AuthManager {
    void auth(WeakReference<Context> context, int code);
    interface Callbacks {
        void authSuccessCallback();
        void authFailedCallback(String message);
    }
}

