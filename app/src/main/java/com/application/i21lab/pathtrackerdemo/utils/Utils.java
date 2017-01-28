package com.application.i21lab.pathtrackerdemo.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    private static final String TAG = "UTILS";

    public static void hideKeyboard(Context context) {
        if (context != null) {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
