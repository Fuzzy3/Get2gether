package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.ArrayList;
import java.util.List;

public interface NewGroupMVP {

    public interface NewGroupPresenter {

        List<User> getUsersMatchingString(String input);
        List<User> getAllUsers();
        void updateGroup(Group group, String title, String description, List<User> participants);
        List<User> getUsersInGroup(Group group);
        Group newGroup();
        User getUserFromUUID(String UUID);


        void removeMemberFromList(List<User> list, User user);

        void editExistingGroup(String stringExtra);

        void editNewGroup();
    }

    public interface NewGroupView {

        void finished();
        void memberSuccesfullyRemoved(ArrayList<User> newlist);
        void setGroup(Group group);

    }

}
