package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;



public class SelectedGroupIndoorPresenterImpl extends SelectedGroupParentPresenter implements SelectedGroupIndoorMVP.SelectedGroupIndoorPresenter {

    SelectedGroupIndoorMVP.SelectedGroupIndoorView mView;

    public SelectedGroupIndoorPresenterImpl(BaseDatabase database, String groupUIUD, SelectedGroupIndoorMVP.SelectedGroupIndoorView view) {
        super(database, groupUIUD);
        mView = view;
    }

    @Override
    public void getCurrentGroup() {

    }
}
