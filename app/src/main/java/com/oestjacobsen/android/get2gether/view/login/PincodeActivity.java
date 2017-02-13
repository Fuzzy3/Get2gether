package com.oestjacobsen.android.get2gether.view.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.view.BaseActivity;
import com.oestjacobsen.android.get2gether.view.MainMenuActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PincodeActivity extends BaseActivity {

    @BindView(R.id.pincode_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);
        setupView();

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, PincodeActivity.class);
        return i;
    }

    @OnClick(R.id.login_button)
    public void onLoginClick(){
        startActivity(MainMenuActivity.newIntent(this));
    }

    private void setupView(){
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setTitle("");
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
