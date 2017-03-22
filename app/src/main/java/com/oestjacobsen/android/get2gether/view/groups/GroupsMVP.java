package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public interface GroupsMVP {

    public interface GroupsPresenter {
        List<Group> getGroups();
        void setActive(Group group, boolean active);
        boolean isActive(Group group);
        void showActiveGroups();


    }

    public interface GroupsView {


    }


}
