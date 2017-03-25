package com.oestjacobsen.android.get2gether.view.groups;


import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.User;

public abstract class SelectedGroupParentPresenter implements SelectedGroupMVP.SelectedGroupPresenter {
    protected BaseDatabase mDatabase;
    protected UserManager mUserManager;
    protected User mCurrentUser;
    protected Group mCurrentGroup;

    public SelectedGroupParentPresenter(BaseDatabase database, String groupUUID) {
        mDatabase = database;
        mUserManager = UserManagerImpl.get();
        mCurrentUser = mUserManager.getUser();
        mCurrentGroup = mDatabase.getGroupFromUUID(groupUUID);
    }

    public abstract void getCurrentGroup();
}
