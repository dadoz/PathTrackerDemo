package com.application.i21lab.pathtrackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Auth");

        initView();
    }

    /**
     * start intent activity
     */
    private void initView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
