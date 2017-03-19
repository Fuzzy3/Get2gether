package com.oestjacobsen.android.get2gether.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FacebookAuthCredential;
import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements LoginMVP.LoginView {

    private String TAG = MainActivity.class.getSimpleName();
    private LoginMVP.LoginPresenter mPresenter;


    @BindView(R.id.login_toolbar) Toolbar mToolbar;
    @BindView(R.id.username_edit_text)
    EditText mUsernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new LoginPresenterImpl(RealmDatabase.get(this), this);
        setupView();

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
    }

    @OnClick(R.id.continue_button_login)
    public void onClick(){
        String username = mUsernameInput.getText().toString();
        mPresenter.authenticateUsername(username);
    }

    private void setupView(){
        ButterKnife.bind(this);

        if(mUsernameInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void usernameAcquired(String userUUID) {
        startActivity(PincodeActivity.newIntent(this, userUUID));
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
