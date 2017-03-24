package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;


public class SelectedGroupMembersPresenterImpl extends SelectedGroupParentPresenter implements SelectedGroupMembersMVP.SelectedGroupMembersPresenter {

    SelectedGroupMembersMVP.SelectedGroupMembersView mView;

    public SelectedGroupMembersPresenterImpl(BaseDatabase database, String groupUIUD, SelectedGroupMembersMVP.SelectedGroupMembersView view) {
        super(database, groupUIUD);
        mView = view;
    }

    @Override
    public void getCurrentGroup() {

    }
}
