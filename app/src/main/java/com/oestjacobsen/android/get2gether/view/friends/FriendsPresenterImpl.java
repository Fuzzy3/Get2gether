package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public class FriendsPresenterImpl implements FriendsPresenter {

    BaseDatabase mDatabase;

    public FriendsPresenterImpl(BaseDatabase database) {
        mDatabase = database;
    }


    public List<User> getFriends() {
        return mDatabase.getAllUsers();
    }


}
