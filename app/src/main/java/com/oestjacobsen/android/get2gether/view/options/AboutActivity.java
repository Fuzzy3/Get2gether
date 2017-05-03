package com.oestjacobsen.android.get2gether.view.options;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.view.OptionsBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends OptionsBaseActivity {

    @BindView(R.id.about_toolbar) Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);
        setToolbar(mToolbar, "About");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, AboutActivity.class);
        return i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
