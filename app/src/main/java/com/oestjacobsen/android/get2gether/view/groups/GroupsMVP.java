package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public interface GroupsMVP {

    public interface GroupsPresenter {
        void getGroupsAndPending();
        void setActive(Group group, boolean active);
        boolean isActive(Group group);
        void showActiveGroups();


        void addPendingGroup(Group mSelectedGroup);
    }

    public interface GroupsView {


        void showToast(String s);

        void showGroupsAndPending(List<Group> groupsAndPending, int size);
    }


}
