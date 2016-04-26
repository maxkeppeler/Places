package com.mk.places.activity;

import android.app.Activity;
import android.os.Bundle;

import com.afollestad.inquiry.Inquiry;

/**
 * Created by max on 26.04.16.
 */
public class DataBase extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Inquiry.init(getApplicationContext(), "DataBase", 1);
    }

    public static void addRow() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
