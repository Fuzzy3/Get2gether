package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;

public class SelectedGroupInfoPresenterImpl extends SelectedGroupParentPresenter implements SelectedGroupInfoMVP.SelectedGroupInfoPresenter {

    SelectedGroupInfoMVP.SelectedGroupInfoView mView;

    public SelectedGroupInfoPresenterImpl(BaseDatabase database, SelectedGroupInfoMVP.SelectedGroupInfoView view, String groupUIUD) {
        super(database, groupUIUD);
        mView = view;

    }

    @Override
    public void removeUserFromGroup() {
        mDatabase.removeGroupFromUser(mCurrentGroup, mCurrentUser);
        mView.groupRemoved();
    }

    @Override
    public void getCurrentGroup() {
        mView.setCurrentGroup(mCurrentGroup);
    }
}
