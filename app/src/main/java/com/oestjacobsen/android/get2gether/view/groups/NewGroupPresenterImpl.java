package com.oestjacobsen.android.get2gether.view.groups;

import android.util.Log;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.GroupIdHelperClass;
import com.oestjacobsen.android.get2gether.model.TestData;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
import java.util.List;

public class NewGroupPresenterImpl implements NewGroupMVP.NewGroupPresenter {

    private BaseDatabase mDatabase;
    private NewGroupMVP.NewGroupView mView;
    private UserManager mUserManager;
    private User mCurrentUser;


    public NewGroupPresenterImpl(BaseDatabase database, NewGroupMVP.NewGroupView view) {
        mDatabase = database;
        mView = view;
        mUserManager = UserManagerImpl.get();
        mCurrentUser = mUserManager.getUser();
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

    private boolean listContains(List<User> list, User user) {
        for (User friend : list) {
            if(friend.getUUID().equals(user.getUUID())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public List<User> getAllUsers() {
        return TestData.getTestUsers();
    }

    @Override
    public void updateGroup(Group group, String title, String description, List<User> participants) {
        group.setGroupTitle(title);
        group.setGroupDesc(description);
        for(User participant : participants) {
            group.addParticipant(participant);
        }

        mDatabase.updateOrAddGroup(group);

    }

    @Override
    public List<User> getUsersInGroup(Group group) {
        return group.getParticipants();
    }

    @Override
    public Group newGroup() {
        Group group = new Group();
        group.addParticipant(mCurrentUser);
        mDatabase.updateOrAddGroup(group);
        mDatabase.addGroupToUser(group, mCurrentUser);
        return group;
    }


}
