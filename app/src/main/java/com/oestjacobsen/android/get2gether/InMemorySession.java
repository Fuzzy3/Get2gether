package com.oestjacobsen.android.get2gether;


import com.oestjacobsen.android.get2gether.model.User;

public class InMemorySession implements CurrentUser {
    private User mUser;
    private static InMemorySession mInMemorySession;

    private InMemorySession() {

    }

    public static InMemorySession get() {
        if(mInMemorySession == null ){
            mInMemorySession = new InMemorySession();
        }
        return mInMemorySession;
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

