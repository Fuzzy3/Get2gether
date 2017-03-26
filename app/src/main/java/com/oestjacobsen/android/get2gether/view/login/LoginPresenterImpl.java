package com.oestjacobsen.android.get2gether.view.login;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;



public class LoginPresenterImpl implements LoginMVP.LoginPresenter, RealmDatabase.loginCallback {
    BaseDatabase mDatabase;
    LoginMVP.LoginView mLoginView;
    private String mUsername;

    public LoginPresenterImpl(BaseDatabase database, LoginMVP.LoginView loginview) {
        mDatabase = database;
        mLoginView = loginview;
    }

    @Override
    public void authenticateUsername(String username) {
        mUsername = username;
        mDatabase.setLoginCallback(this);
        mDatabase.setupRealmSync();

    }

    @Override
    public void loginSucceded() {
        User loginUser = mDatabase.getUserFromUsername(mUsername.trim());
        if(loginUser != null) {
            mLoginView.usernameAcquired(loginUser.getUUID());
        } else {
            mLoginView.showToast("Username not found");
        }
    }
}
