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

    /**
     *
     * @param view
     * @param message
     * @param isError
     * @param action
     * @param listener
     * @return
     */
    public static  Snackbar getSnackBarWithAction(@NonNull View view, String message, boolean isError,
                                                  String action, View.OnClickListener listener) {
        Snackbar snackbar = getSnackBar(view, message, isError, Snackbar.LENGTH_INDEFINITE);
        if (snackbar != null) {
            snackbar.setAction(action, listener);
        }
        return snackbar;
    }

    /**
     *
     * @param view
     * @param message
     * @param isError
     * @param length
     * @return
     */
    public static  Snackbar getSnackBar(@NonNull View view, String message, boolean isError, int length) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, length);
            snackbar.getView().setBackgroundResource(isError ? R.color.colorPrimary : R.color.grey_900);
            return snackbar;
        }
        return null;
    }
}
