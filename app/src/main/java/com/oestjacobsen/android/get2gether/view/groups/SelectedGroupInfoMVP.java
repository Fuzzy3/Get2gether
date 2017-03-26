package com.oestjacobsen.android.get2gether.view.groups;


import com.oestjacobsen.android.get2gether.model.Group;

public interface SelectedGroupInfoMVP {
    public interface SelectedGroupInfoPresenter {
        void getCurrentGroup();

        void removeUserFromGroup();
    }

    public interface SelectedGroupInfoView {

        void setCurrentGroup(Group mCurrentGroup);

        void groupRemoved();
    }
}
