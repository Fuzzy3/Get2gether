package com.oestjacobsen.android.get2gether.view.friends;


import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;

public interface AddFriendPresenter {

    List<User> getUsersMatchingString(String input);

}
