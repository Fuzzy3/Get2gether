package com.oestjacobsen.android.get2gether.model;


import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class AuthRealm implements SyncUser.Callback {

    //USERNAME AND PASSWORD
    private static final String USERNAME = "soest@itu.dk";
    private static final String PASSWORD = "soebach#Jac";

    //ONLINE SERVER STUFF
    private static final String HOST_ITU = "130.226.142.162";
    private static final String HOST_LOCAL = "SOeXPS";
    private static final String HOST= HOST_ITU;
    private static final String DBNAME = "realmthings";
    private static final String INITIALS = "soeojac";

    //Server URL
    private static final String AUTH_URL = "http://" + HOST + ":9080/auth";
    public static final String REALM_URL="realm://" + HOST + ":9080/~/" + INITIALS + DBNAME;

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

    public void authenticateUser() {
        if(SyncUser.currentUser() == null) {
            SyncCredentials myCredentials = SyncCredentials.usernamePassword(USERNAME, PASSWORD, false);
            SyncUser.loginAsync(myCredentials, AUTH_URL, this);
        } else {
            setupSync(SyncUser.currentUser());
        }
    }



    @Override
    public void onSuccess(SyncUser user) {

    }

    @Override
    public void onError(ObjectServerError error) {
        Log.i(TAG, "Failed to login to Online server, Using offline instead \nError: " + error.getErrorMessage());
    }
}
