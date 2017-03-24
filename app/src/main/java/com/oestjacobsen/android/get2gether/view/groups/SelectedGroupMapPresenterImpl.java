package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;


public class SelectedGroupMapPresenterImpl extends SelectedGroupParentPresenter implements SelectedGroupMapMVP.SelectedGroupMapPresenter {

    SelectedGroupMapMVP.SelectedGroupMapView mView;

    public SelectedGroupMapPresenterImpl(BaseDatabase database, String groupUIUD, SelectedGroupMapMVP.SelectedGroupMapView view) {
        super(database, groupUIUD);
        mView = view;
    }

    @Override
    public void getCurrentGroup() {

    }
}
