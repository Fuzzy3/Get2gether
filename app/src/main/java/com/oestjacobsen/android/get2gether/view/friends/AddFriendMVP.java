package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public interface AddFriendMVP {
    public interface AddFriendPresenter {

        List<User> getUsersMatchingString(String input);

        List<User> getAllUsers();

    }

    public interface AddFriendView {

    }

}
