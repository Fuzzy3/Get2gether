package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public interface FriendsMVP {

    public interface FriendsPresenter {

        List<User> getFriends();
    }

    public interface FriendsView {


    }

}
