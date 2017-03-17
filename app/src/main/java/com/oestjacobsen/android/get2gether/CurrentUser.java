package com.oestjacobsen.android.get2gether;

import com.oestjacobsen.android.get2gether.model.User;


public interface CurrentUser {
    boolean isLoggedIn();
    User getUser();
    void setCurrentUser(User user);
    void loggedOff();

}
