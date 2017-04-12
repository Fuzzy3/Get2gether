package com.oestjacobsen.android.get2gether.view.groups;


import com.oestjacobsen.android.get2gether.model.Group;

public interface SelectedGroupIndoorMVP {
    public interface SelectedGroupIndoorPresenter {
        void getCurrentGroup();

    }

    public interface SelectedGroupIndoorView {
        void setCurrentGroup(Group mCurrentGroup);

    }

}
