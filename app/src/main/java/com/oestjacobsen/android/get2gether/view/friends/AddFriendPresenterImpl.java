package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.model.TestData;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddFriendPresenterImpl implements AddFriendPresenter {


    @Override
    public List<User> getUsersMatchingString(String input) {
        List<User> UsersMatching = new ArrayList<>();

        if (input.equals(null) || input.equals("")) {
            return UsersMatching;
        } else {
            for (User u : TestData.getTestUsers()) {
                if (input.length() == 1 && input.trim().toLowerCase().startsWith(u.getFullName().trim().toLowerCase().substring(0,1)) ||
                        input.length() > 1 && u.getFullName().trim().toLowerCase().contains(input.trim().toLowerCase())) {
                    UsersMatching.add(u);
                }
            }
        }
        return UsersMatching;
    }


}
