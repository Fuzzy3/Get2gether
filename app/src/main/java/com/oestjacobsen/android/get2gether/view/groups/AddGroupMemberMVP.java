package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;



public interface AddGroupMemberMVP {


    public interface AddGroupMemberPresenter {

        List<User> getUsersMatchingString(String input);

        List<User> getAllUsers();

        void addMember(Group group, User member);
    }

        public interface AddGroupMemberView {


        }
}
