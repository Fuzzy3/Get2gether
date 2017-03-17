package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.CurrentUser;
import com.oestjacobsen.android.get2gether.InMemorySession;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.Collection;
import java.util.List;


public class FriendsPresenterImpl implements FriendsMVP.FriendsPresenter {

    private BaseDatabase mDatabase;
    private FriendsMVP.FriendsView mView;
    private CurrentUser mSessionUser;
    private User mCurrentUser;


    public FriendsPresenterImpl(BaseDatabase database, FriendsMVP.FriendsView view) {
        mDatabase = database;
        mView = view;

        mSessionUser = InMemorySession.get();
        mCurrentUser = mSessionUser.getUser();
    }


    public List<User> getFriends() {
        return mCurrentUser.getFriends();
    }


}
