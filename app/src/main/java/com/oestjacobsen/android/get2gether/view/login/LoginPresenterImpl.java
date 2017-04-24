package com.oestjacobsen.android.get2gether.view.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.oestjacobsen.android.get2gether.DatabasePicker;
import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.AuthRealm;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import io.realm.Realm;


public class LoginPresenterImpl implements LoginMVP.LoginPresenter, RealmDatabase.loginCallback {
    private static final String TAG = "LoginPresenter";
    BaseDatabase mDatabase;
    LoginMVP.LoginView mView;
    private String mUsername;
    private Context mLoginContext;
    private Bundle mFacebookData;
    private UserManager mUserManager;
    private AuthRealm mAuthRealm;


    public LoginPresenterImpl(Context context, LoginMVP.LoginView loginview) {
        Realm.init(context);
        mLoginContext = context;
        mView = loginview;
        mUserManager = UserManagerImpl.get();
        mAuthRealm = new AuthRealm();
    }

    /*@Override
    public void authenticateUsername(String username) {
        mUsername = username;
        mDatabase.setLoginCallback(this);
        mDatabase.setupDatabaseSync();
    }*/

    @Override
    public void authenticateFacebook(AccessToken accessToken, Bundle facebookData) {
        mFacebookData = facebookData;
        mAuthRealm.setLoginCallback(this);
        mAuthRealm.authenticateUser();

    }

    /*@Override
    public void populateDatabase() {
        String userID = "1234";
        String userName = "Søren Oest Jacobsen";
        User newUser = new User(userID);
        newUser.setUsername(userName);
        mDatabase.addUser(newUser);

    }*/

    @Override
    public void loginSucceded() {
        Log.i(TAG, "Logged in to server");
        mDatabase = DatabasePicker.getChosenDatabase(mLoginContext);
        //
        String userId = mFacebookData.getString("idFacebook");
        User newUser = mDatabase.getUserFromUUID(userId);
        if(newUser == null) {
            newUser = new User(userId);
            String fullname = mFacebookData.getString("first_name") + " " + mFacebookData.getString("last_name");
            newUser.setFullName(fullname);
            mDatabase.addUser(newUser);
        }
        mUserManager.setCurrentUser(newUser);
        mView.usernameAcquired(newUser.getUUID());
    }

}
