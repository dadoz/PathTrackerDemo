package com.application.i21lab.pathtrackerdemo.factory;

import android.app.KeyguardManager;
import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import java.lang.ref.WeakReference;

public class AuthFactory {

    /**
     *
     * @param context
     * @param lst
     * @return
     */
    public static AuthManager getAuthManager(WeakReference<Context> context,
                                             WeakReference<AuthManager.Callbacks> lst) {
        if (context.get() != null &&
                isFingerprintEnabled(context)) {
            return new FingerprintManager(lst, context.get());
        }
        return new PincodeManager(lst);
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isFingerprintEnabled(WeakReference<Context> context) {
        return context.get() != null &&
                FingerprintManagerCompat.from(context.get()).isHardwareDetected() &&
                FingerprintManagerCompat.from(context.get())
                    .hasEnrolledFingerprints() &&
                ((KeyguardManager) context.get().getSystemService(Context.KEYGUARD_SERVICE)).isKeyguardSecure();
    }
}
