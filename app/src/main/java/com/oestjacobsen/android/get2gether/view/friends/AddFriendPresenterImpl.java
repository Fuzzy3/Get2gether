package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.CurrentUser;
import com.oestjacobsen.android.get2gether.InMemorySession;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.TestData;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddFriendPresenterImpl implements AddFriendMVP.AddFriendPresenter {

    private BaseDatabase mDatabase;
    private AddFriendMVP.AddFriendView mView;
    private CurrentUser mSessionUser;
    private User mCurrentUser;


    public AddFriendPresenterImpl(BaseDatabase database, AddFriendMVP.AddFriendView view) {
        mDatabase = database;
        mView = view;

        mSessionUser = InMemorySession.get();
        mCurrentUser = mSessionUser.getUser();
    }

    @Override
    public List<User> getUsersMatchingString(String input) {

        String uppercaseInput = input.substring(0, 1).toUpperCase() + input.substring(1);
        List<User> UsersMatching =  mDatabase.getUsersMatchingString(uppercaseInput);

        /*if (input.equals(null) || input.equals("")) {
            return UsersMatching;
        } else {
            for (User u : TestData.getTestUsers()) {
                if (input.length() == 1 && input.trim().toLowerCase().startsWith(u.getFullName().trim().toLowerCase().substring(0,1)) ||
                        input.length() > 1 && u.getFullName().trim().toLowerCase().contains(input.trim().toLowerCase())) {
                    UsersMatching.add(u);
                }
            }
        }*/

        return UsersMatching;
    }

    @Override
    public List<User> getAllUsers() {
        return TestData.getTestUsers();
    }


}
