package com.oestjacobsen.android.get2gether.view.login;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;



public class LoginPresenterImpl implements LoginMVP.LoginPresenter {
    BaseDatabase mDatabase;
    LoginMVP.LoginView mLoginView;

    public LoginPresenterImpl(BaseDatabase database, LoginMVP.LoginView loginview) {
        mDatabase = database;
        mLoginView = loginview;
    }

    @Override
    public void authenticateUsername(String username) {
        User loginUser = mDatabase.getUserFromUsername(username.trim());
        if(loginUser != null) {
            mLoginView.usernameAcquired(loginUser.getUUID());
        } else {
            mLoginView.showToast("Username not found");
        }
    }
}
