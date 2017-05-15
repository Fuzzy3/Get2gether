package com.oestjacobsen.android.get2gether;


import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.GroupIdHelperClass;
import com.oestjacobsen.android.get2gether.model.User;

public class UserManagerImpl implements UserManager {
    private User mUser;
    private String mUserUUID;
    private static UserManagerImpl mUserManagerImpl;

    private UserManagerImpl() {
    }

    public static UserManagerImpl get() {
        if(mUserManagerImpl == null ){
            mUserManagerImpl = new UserManagerImpl();
        }
        return mUserManagerImpl;
    }

    @Override
    public void setCurrentUser(User user){
        mUser = user;
    }

    public void loggedOff(){
        setCurrentUser(null);
    }

    @Override
    public boolean isLoggedIn() {
        return mUser != null;
    }

    @Override
    public User getUser() {
        return mUser;
    }
}

