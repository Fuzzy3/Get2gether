package com.oestjacobsen.android.get2gether.view.groups;


import android.location.Location;

import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.HashMap;

public interface SelectedGroupMapMVP {
    public interface SelectedGroupMapPresenter {
        void getCurrentGroup();

        void getActiveGroups();

        void getCurrentUser();
    }

    public interface SelectedGroupMapView {

        void setActiveHashMap(HashMap<String, Boolean> map);

        void setCurrentGroup(Group mCurrentGroup);

        void setCurrentUser(User user);
    }
}
