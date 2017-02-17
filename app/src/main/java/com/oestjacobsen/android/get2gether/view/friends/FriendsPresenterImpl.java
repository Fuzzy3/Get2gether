package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.model.TestData;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public class FriendsPresenterImpl implements FriendsPresenter {



    public List<User> getFriends() {
        return TestData.getTestUsers();
    }


}
