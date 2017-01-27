package com.application.i21lab.pathtrackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;

import com.application.i21lab.pathtrackerdemo.factory.AuthFactory;
import com.application.i21lab.pathtrackerdemo.factory.AuthManager;

import java.lang.ref.WeakReference;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AuthFactory.isFingerprintEnabled(new WeakReference<>(getBaseContext()))) {
            setContentView(R.layout.activity_fingerprint_layout);
        } else {
            setContentView(R.layout.activity_pincode_layout);
        }

        getSupportActionBar().hide();
//        initView();
    }

    /**
     * start intent activity
     */
    private void initView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
