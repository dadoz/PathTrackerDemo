package com.application.i21lab.pathtrackerdemo;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.i21lab.pathtrackerdemo.factory.AuthFactory;
import com.application.i21lab.pathtrackerdemo.factory.AuthManager;
import com.application.i21lab.pathtrackerdemo.helpers.RequestPermissionHelper;
import com.application.i21lab.pathtrackerdemo.utils.Utils;

import java.lang.ref.WeakReference;
import java.security.Permission;
import java.security.PermissionCollection;

import static android.Manifest.permission.USE_FINGERPRINT;
import static com.application.i21lab.pathtrackerdemo.helpers.RequestPermissionHelper.FINGERPRINT_REQUEST_CODE;

public class AuthenticationActivity extends AppCompatActivity implements AuthManager.Callbacks,
        View.OnClickListener,  RequestPermissionHelper.RequestPermissionCallbacks {

    private AuthManager authManager;
    private String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFingerprintEnabled = (AuthFactory.isFingerprintEnabled(new WeakReference<>(getBaseContext())));
        setContentView(isFingerprintEnabled ? R.layout.activity_fingerprint_layout : R.layout.activity_pincode_layout);

        getSupportActionBar().hide();
        authManager = AuthFactory.getAuthManager(new WeakReference<>(getApplicationContext()),
                new WeakReference<AuthManager.Callbacks>(this));

        initView(savedInstanceState, isFingerprintEnabled);
    }

    private void initView(Bundle savedInstanceState, boolean isFingerprintEnabled) {
        if (isFingerprintEnabled) {
            initViewFingerprint();
            return;
        }
        initViewPincode();
    }

    /**
     * start intent activity
     */
    public void initViewPincode() {
        View button = findViewById(R.id.pincodeAuthButtonId);
        if (button != null) {
            button.setOnClickListener(this);
        }
    }

    /**
     *
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void initViewFingerprint() {
        if (!RequestPermissionHelper.requestPermission(new WeakReference<Activity>(this), USE_FINGERPRINT,
                FINGERPRINT_REQUEST_CODE)) {
            onPermissionGrantedSuccessCb();
        }
    }

    @Override
    public void authSuccessCallback() {
        Toast.makeText(getApplicationContext(), getString(R.string.hello), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void authFailedCallback(String message) {
        try {
            String msg = message == null ? getString(R.string.bad_auth_message) : message;
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(R.id.mainLayoutId),
                    msg, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey_50));
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                    .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pincodeAuthButtonId:
                EditText editText = ((TextInputLayout) findViewById(R.id.pinTextInputLayoutId))
                        .getEditText();
                int code = editText.getText().toString().equals("") ? -1 :
                        Integer.parseInt(editText.getText().toString());
                authManager.auth(new WeakReference<Context>(getApplicationContext()), code);
                Utils.hideKeyboard(getApplicationContext(), getCurrentFocus());
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        RequestPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults,
                new WeakReference<RequestPermissionHelper.RequestPermissionCallbacks>(this));
    }

    @Override
    public void onPermissionGrantedSuccessCb() {
        Log.e(TAG, "OK");
        if (authManager != null)
            authManager.auth(new WeakReference<Context>(getApplicationContext()), -1);
    }

    @Override
    public void onPermissionGrantedFailureCb() {

        Log.e(TAG, "error");
        authFailedCallback(null);
    }
}
