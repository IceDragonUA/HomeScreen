package com.example.icedr.homescreendemo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.icedr.homescreendemo.delegates.HomeCategory1;
import com.example.icedr.homescreendemo.delegates.HomeCategory2;
import com.example.icedr.homescreendemo.delegates.HomeCategory3;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    protected View mRootView;

    @Bind(R.id.browse_list)
    RecyclerView browseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);
        mRootView = findViewById(R.id.activity_main);
        ButterKnife.bind(this, mRootView);
        browseList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CompositeAdapter mainAdapter = new CompositeAdapter.Builder()
                .addAdapter(new HomeCategory3(this))
                .addAdapter(new HomeCategory1(this))
                .addAdapter(new HomeCategory2(this))
                .addAdapter(new HomeCategory3(this))
                .addAdapter(new HomeCategory1(this))
                .addAdapter(new HomeCategory2(this))
                .addAdapter(new HomeCategory3(this))
                .addAdapter(new HomeCategory1(this))
                .addAdapter(new HomeCategory2(this))
                .build();
        browseList.setAdapter(mainAdapter);
    }
}
