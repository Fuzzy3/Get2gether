package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.TestData;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
import java.util.List;


public class AddGroupMemberPresenterImpl implements AddGroupMemberMVP.AddGroupMemberPresenter {

    private BaseDatabase mDatabase;
    private AddGroupMemberMVP.AddGroupMemberView mView;
    private UserManager mUserManager;
    private User mCurrentUser;


    public AddGroupMemberPresenterImpl(BaseDatabase database, AddGroupMemberMVP.AddGroupMemberView view) {
        mDatabase = database;
        mView = view;
        mUserManager = UserManagerImpl.get();
        mCurrentUser = mUserManager.getUser();
    }

    @Override
    public List<User> getUsersMatchingString(String input) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void addMember(Group group, User member) {

    }
}

