package com.oestjacobsen.android.get2gether.view.groups;


import android.util.Log;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.GroupIdHelperClass;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
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
    public void getGroupsAndPending() {
        List<Group> groupsAndPending = new ArrayList<>();
        for(Group group : mCurrentUser.getGroups()) {
            groupsAndPending.add(group);
        }
        for(Group pending: mCurrentUser.getPendingGroupInvites()) {
            groupsAndPending.add(pending);
        }

        mView.showGroupsAndPending(groupsAndPending, mCurrentUser.getGroups().size());
    }

    @Override
    public void setActive(Group group, boolean active) {
        mDatabase.setActiveGroup(mCurrentUser, group, active);
    }

    @Override
    public boolean isActive(Group group) {
        for(GroupIdHelperClass groupHelper : mCurrentUser.getActiveGroups()) {
            if(groupHelper.getGroupUUID().equals(group.getUUID())) {
                return groupHelper.isActive();
            }
        }
        Log.i(group.getGroupTitle() + "", "didn't find id");
        return false;
    }

    @Override
    public void addPendingGroup(Group group) {
        if(!mCurrentUser.getPendingGroupInvites().contains(group)) {
            return;
        }
        mDatabase.addPendingGroup(mCurrentUser, group);
        mView.showToast(group.getGroupTitle() + "added to your list of groups");
        getGroupsAndPending();
    }

    @Override
    public void showActiveGroups() {
        for(Group group : mCurrentUser.getGroups()) {
            Log.i("PRINTING GROUPS", group.getGroupTitle());
        }
        Log.i("-------BREAK-----", "-------BREAK------");
        for(GroupIdHelperClass id : mCurrentUser.getActiveGroups()) {
            Log.i(id.isActive() + "", id.getGroupUUID());
            Log.i("ownuuid", id.getUUID() + "");
            Log.i("new", "-----");
        }

    }
}
