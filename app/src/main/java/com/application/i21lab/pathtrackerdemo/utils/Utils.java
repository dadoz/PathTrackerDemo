package com.application.i21lab.pathtrackerdemo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.application.i21lab.pathtrackerdemo.R;

public class Utils {
    private static final String TAG = "UTILS";

    public static void hideKeyboard(Context context, View view) {
        if (context != null) {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager.isActive(view))
                manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static  Snackbar getSnackBar(@NonNull View view, String message, boolean isError) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundResource(isError ? R.color.colorPrimary : R.color.grey_900);
            return snackbar;
        }
        return null;
    }
}
