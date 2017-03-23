package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public interface FriendsMVP {

    public interface FriendsPresenter {

        void getFriendsAndPending();

        void addPendingFriend(User mSelectedUser);
    }

    public interface FriendsView {

        void showFriendsAndPending(List<User> friendsandpending, int pendingStartingPosition);

        void showToast(String msg);
    }

}
