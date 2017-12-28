package com.github.leenjewel.cocos2dx_in_android_native;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            System.loadLibrary("cocos2djs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
