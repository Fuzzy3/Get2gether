package com.oestjacobsen.android.get2gether;


import com.oestjacobsen.android.get2gether.model.User;

public class UserManagerImpl implements UserManager {
    private User mUser;
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

