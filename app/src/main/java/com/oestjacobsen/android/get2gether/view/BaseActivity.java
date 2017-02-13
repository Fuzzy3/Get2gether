package com.oestjacobsen.android.get2gether.view;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import icepick.Icepick;
import icepick.State;

public abstract class BaseActivity extends AppCompatActivity {
    @State
    String State;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


}
