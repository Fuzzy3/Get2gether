package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
import java.util.List;


public class FriendsPresenterImpl implements FriendsMVP.FriendsPresenter {

    private BaseDatabase mDatabase;
    private FriendsMVP.FriendsView mView;
    private UserManager mSessionUser;
    private User mCurrentUser;


    public FriendsPresenterImpl(BaseDatabase database, FriendsMVP.FriendsView view) {
        mDatabase = database;
        mView = view;

        mSessionUser = UserManagerImpl.get();
        mCurrentUser = mSessionUser.getUser();
    }


    public void getFriendsAndPending() {
        List<User> friendsAndPending = new ArrayList<>();
        for(User friend : mCurrentUser.getFriends()) {
            friendsAndPending.add(friend);
        }
        for(User pending : mCurrentUser.getPendingInvites()) {
            friendsAndPending.add(pending);
        }

        mView.showFriendsAndPending(friendsAndPending, mCurrentUser.getFriends().size());
    }

    @Override
    public void addPendingFriend(User friend) {
        if (!mCurrentUser.getPendingInvites().contains(friend)) {
            return;
        }
        mDatabase.addPendingFriend(mCurrentUser, friend);
        mView.showToast(friend.getFullName() + " added to your friend list");
        getFriendsAndPending();
    }


}
