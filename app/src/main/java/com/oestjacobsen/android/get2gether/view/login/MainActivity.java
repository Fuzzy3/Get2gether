package com.oestjacobsen.android.get2gether.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.login_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
    }

    @OnClick(R.id.continue_button_login)
    public void onClick(){
        startActivity(PincodeActivity.newIntent(this));
    }

    private void setupView(){
        ButterKnife.bind(this);

    }

}
