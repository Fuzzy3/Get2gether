package com.oestjacobsen.android.get2gether.view.options;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpActivity extends UserBaseActivity {

    @BindView(R.id.help_toolbar) Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setupView();
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, HelpActivity.class);
        return i;
    }

    private void setupView() {
        ButterKnife.bind(this);
        setToolbar(mToolbar, "Help");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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
