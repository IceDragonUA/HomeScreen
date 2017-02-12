package com.example.icedr.homescreendemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.example.icedr.homescreendemo.R;

/**
 * Created by IceDr on 12.02.2017.
 */

public class ErrorActivity extends Activity {

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exit) {
            finish();
        } else {
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_error);
    }
}
