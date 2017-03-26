package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.Group;


public interface SelectedGroupMVP {
    public interface SelectedGroupPresenter {
        void getCurrentGroup();
    }
    public interface SelectedGroupView {
        void showToast(String msg);
        void setCurrentGroup(Group group);
    }
}
