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
        updateUserInfo();
        List<User> friendsAndPending = new ArrayList<>();
        for(User friend : mCurrentUser.getFriends()) {
            friendsAndPending.add(friend);
        }
        for(User pending : mCurrentUser.getPendingInvites()) {
            if(mCurrentUser.getFriends().isEmpty()) {
                friendsAndPending.add(pending);
            } else {
                boolean duplicate = false;
                for(User friend : mCurrentUser.getFriends()) {
                    if(friend.getUUID().equals(pending.getUUID())) {
                        duplicate = true;
                        break;
                    }
                }
                if(!duplicate){
                    friendsAndPending.add(pending);
                }
            }

        }

        mView.showFriendsAndPending(friendsAndPending, mCurrentUser.getFriends().size());
    }

    private void updateUserInfo() {
        User user = mDatabase.getUserFromUUID(mCurrentUser.getUUID());
        if(user != null) {
            mSessionUser.setCurrentUser(user);
        }
    }

    @Override
    public void addPendingFriend(User friend) {
        for(User pending : mCurrentUser.getPendingInvites()) {
            if(!pending.getUUID().equals(friend.getUUID())) {
                return;
            }
        }

        mDatabase.addPendingFriend(mCurrentUser, friend);
        mView.showToast(friend.getFullName() + " added to your friend list");
        getFriendsAndPending();
    }


}
