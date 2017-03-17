package com.oestjacobsen.android.get2gether.view.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.view.BaseActivity;
import com.oestjacobsen.android.get2gether.view.MainMenuActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PincodeActivity extends BaseActivity implements PincodeMVP.PincodeView {

    @BindView(R.id.pincode_toolbar) Toolbar mToolbar;
    @BindView(R.id.enter_pincode) EditText mPasswordInput;

    private PincodeMVP.PincodePresenter mPresenter;

    private static String ARGS_UUID = "ARGS_USER_UUID";
    private String UserUUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);
        mPresenter = new PincodePresenterImpl(RealmDatabase.get(this), this);

        setupView();
    }

    public static Intent newIntent(Context packageContext, String UUID) {
        Intent i = new Intent(packageContext, PincodeActivity.class);
        i.putExtra(ARGS_UUID, UUID);
        return i;
    }

    @OnClick(R.id.login_button)
    public void onLoginClick(){
        mPresenter.authenticatePassword(getIntent().getStringExtra(ARGS_UUID), mPasswordInput.getText().toString());
    }

    public void passwordSuccesful() {
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


    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
