package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.TestData;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddFriendPresenterImpl implements AddFriendMVP.AddFriendPresenter {

    private BaseDatabase mDatabase;
    private AddFriendMVP.AddFriendView mView;
    private UserManager mSessionUser;
    private User mCurrentUser;


    public AddFriendPresenterImpl(BaseDatabase database, AddFriendMVP.AddFriendView view) {
        mDatabase = database;
        mView = view;

        mSessionUser = UserManagerImpl.get();
        mCurrentUser = mSessionUser.getUser();
    }

    @Override
    public List<User> getUsersMatchingString(String input) {

        String uppercaseInput = input.substring(0, 1).toUpperCase() + input.substring(1); //First letter to uppercase before query
        List<User> usersMatching =  mDatabase.getUsersMatchingString(uppercaseInput);
        List<User> filterFriendsList = new ArrayList<>();

        for(User friend : usersMatching) {
            if(!listContains(mCurrentUser.getFriends(), friend)) {
                filterFriendsList.add(friend);
            }
        }

        return filterFriendsList;
    }



    @Override
    public List<User> getAllUsers() {
        return TestData.getTestUsers();
    }

    @Override
    public void addFriend(User friend) {
        //Checking if friend is already a friend
        for(User friendInList : mCurrentUser.getFriends()) {
            if(friendInList.getUUID().equals(friend.getUUID())) {
                return;
            }
        }

        mDatabase.addFriend(mCurrentUser, friend);
        mView.updateUI();
    }

    private boolean listContains(List<User> list, User user) {
        for (User friend : list) {
            if(friend.getUUID().equals(user.getUUID())) {
                return true;
            }
        }
        return false;
    }

}
