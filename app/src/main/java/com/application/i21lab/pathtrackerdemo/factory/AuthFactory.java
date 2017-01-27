package com.application.i21lab.pathtrackerdemo.factory;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import java.lang.ref.WeakReference;

public class AuthFactory {

    /**
     *
     * @param context
     * @return
     */
    public static AuthManager getAuthManager(WeakReference<Context> context) {
        if (context.get() == null) {
            return new PincodeManager();
        }

        if (FingerprintManagerCompat.from(context.get())
                .isHardwareDetected() && FingerprintManagerCompat.from(context.get())
                .hasEnrolledFingerprints()) {
            return new FingerprintManager();
        }
        return new PincodeManager();
    }

    public static boolean isFingerprintEnabled(WeakReference<Context> context) {
        return context.get() != null &&
                FingerprintManagerCompat.from(context.get()).isHardwareDetected() &&
                FingerprintManagerCompat.from(context.get())
                    .hasEnrolledFingerprints();
    }
}
