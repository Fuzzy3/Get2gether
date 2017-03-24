package com.oestjacobsen.android.get2gether.view.groups;


import com.oestjacobsen.android.get2gether.model.Group;

public interface SelectedGroupInfoMVP {
    public interface SelectedGroupInfoPresenter {
        void getCurrentGroup();
    }

    public interface SelectedGroupInfoView {

        void setCurrentGroup(Group mCurrentGroup);
    }
}
