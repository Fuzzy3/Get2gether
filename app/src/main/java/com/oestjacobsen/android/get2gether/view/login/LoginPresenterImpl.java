package com.oestjacobsen.android.get2gether.view.login;

import android.util.Log;

import com.facebook.AccessToken;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;



public class LoginPresenterImpl implements LoginMVP.LoginPresenter, RealmDatabase.loginCallback {
    private static final String TAG = "LoginPresenter";
    BaseDatabase mDatabase;
    LoginMVP.LoginView mView;
    private String mUsername;

    public LoginPresenterImpl(BaseDatabase database, LoginMVP.LoginView loginview) {
        mDatabase = database;
        mView = loginview;
    }

    @Override
    public void authenticateUsername(String username) {
        mUsername = username;
        mDatabase.setLoginCallback(this);
        mDatabase.setupRealmSync();
    }

    @Override
    public void authenticateFacebook(AccessToken accessToken) {
        //mDatabase.setLoginCallback(this);
        //mDatabase.setupRealmSync();
        //mDatabase.setupRealmSyncWithFacebook(accessToken);
        mView.showToast("Authenticate with facebook currently not working");
    }

    @Override
    public void loginSucceded() {
        Log.i(TAG, "Logged in to server");
        //
        User loginUser = mDatabase.getUserFromUsername(mUsername.trim());
        if(loginUser != null) {
            mView.usernameAcquired(loginUser.getUUID());
        } else {
            mView.showToast("Username not found");
        }
    }
}
