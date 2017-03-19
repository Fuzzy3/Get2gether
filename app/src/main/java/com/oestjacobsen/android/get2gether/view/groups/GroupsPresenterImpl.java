package com.oestjacobsen.android.get2gether.view.groups;


import android.util.Log;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.GroupIdHelperClass;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;

public class GroupsPresenterImpl implements GroupsMVP.GroupsPresenter {

    private BaseDatabase mDatabase;
    private GroupsMVP.GroupsView mView;
    private UserManager mUserManager;
    private User mCurrentUser;


    public GroupsPresenterImpl(BaseDatabase database, GroupsMVP.GroupsView view) {
        mDatabase = database;
        mView = view;
        mUserManager = UserManagerImpl.get();
        mCurrentUser = mUserManager.getUser();
    }

    @Override
    public List<Group> getGroups() {
        return mCurrentUser.getGroups();
    }

    @Override
    public void setActive(Group group, boolean active) {
        mDatabase.setActiveGroup(mCurrentUser, group, active);
    }

    @Override
    public boolean getActive(Group group) {
        return mCurrentUser.checkActive(group);
    }

    @Override
    public void showActiveGroups() {
        for(GroupIdHelperClass id : mCurrentUser.getActiveGroups()) {
            for(Group group : mCurrentUser.getGroups()) {
                if(id.toString().equals(group.getUUID())) {
                    Log.d("TEST", "Active: " + group.getGroupTitle());
                }
            }
        }
    }
}
