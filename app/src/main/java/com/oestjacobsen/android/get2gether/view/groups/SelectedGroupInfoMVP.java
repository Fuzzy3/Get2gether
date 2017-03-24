package com.oestjacobsen.android.get2gether.view.groups;


import com.oestjacobsen.android.get2gether.model.Group;

public interface SelectedGroupInfoMVP {
    public interface SelectedGroupInfoPresenter {

    }

    public interface SelectedGroupInfoView {

        void setCurrentGroup(Group mCurrentGroup);
    }
}
