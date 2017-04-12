package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;



public class SelectedGroupIndoorPresenterImpl extends SelectedGroupParentPresenter implements SelectedGroupIndoorMVP.SelectedGroupIndoorPresenter {

    SelectedGroupIndoorMVP.SelectedGroupIndoorView mView;

    public SelectedGroupIndoorPresenterImpl(BaseDatabase database, SelectedGroupIndoorMVP.SelectedGroupIndoorView view, String groupUUID) {
        super(database, groupUUID);
        mView = view;
    }


    @Override
    public void getCurrentGroup() {
        mView.setCurrentGroup(mCurrentGroup);
    }
}
