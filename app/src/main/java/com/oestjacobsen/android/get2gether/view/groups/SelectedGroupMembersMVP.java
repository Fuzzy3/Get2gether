package com.oestjacobsen.android.get2gether.view.groups;


import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.HashMap;
import java.util.List;

public interface SelectedGroupMembersMVP {
    public interface SelectedGroupMembersPresenter {
        void getCurrentGroup();
        void getActiveGroups();
    }

    public interface SelectedGroupMembersView {
        void setActiveHashMap(HashMap<String, Boolean> map);
        void setCurrentGroup(Group group);
        void setNumberOfActive(int num);
        void setNumberOfInActive(int num);
    }
}
