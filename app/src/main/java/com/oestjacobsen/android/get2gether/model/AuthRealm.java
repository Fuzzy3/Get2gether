package com.oestjacobsen.android.get2gether.model;


import android.content.Context;
import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class AuthRealm implements SyncUser.Callback {

    private Context mContext;

    //USERNAME AND PASSWORD
    private static final String USERNAME = "soest@itu.dk";
    private static final String PASSWORD = "soebach#Jac";

    //ONLINE SERVER STUFF
    private static final String HOST_ITU = "130.226.142.162";
    private static final String HOST_LOCAL = "SOeXPS";
    private static final String HOST= HOST_ITU;
    private static final String DBNAME = "get2gether";
    private static final String TESTNUMBER = "4";

    //Server URL
    private static final String AUTH_URL = "http://" + HOST + ":9080/auth";
    public static final String REALM_URL="realm://" + HOST + ":9080/~/" + DBNAME + TESTNUMBER;


    private static final String TAG = "AuthRealm";
    private BaseDatabase.loginCallback mLoginCallback;

    public void setLoginCallback(BaseDatabase.loginCallback callback) {
        mLoginCallback = callback;
    }

    private void setupSync(SyncUser user) {
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, REALM_URL).build();
        Realm.setDefaultConfiguration(defaultConfig);

        Log.i(TAG, "Login succeded");
        mLoginCallback.loginSucceded();
    }

    public void authenticateUser(Context context) {
        Realm.init(context);
        if(mContext == null) {
            mContext = context;
        }

        if(SyncUser.currentUser() == null) {
            SyncCredentials myCredentials = SyncCredentials.usernamePassword(USERNAME, PASSWORD, false);
            SyncUser.loginAsync(myCredentials, AUTH_URL, this);
        } else {
            setupSync(SyncUser.currentUser());
        }
    }

    @Override
    public void onSuccess(SyncUser user) {
        setupSync(SyncUser.currentUser());
    }

    @Override
    public void onError(ObjectServerError error) {
        Log.i(TAG, "Failed to login to Online server, Using offline instead \nError: " + error.getErrorMessage());
        authenticateUser(mContext);
    }
}
