package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;

/**
 * Created by Oest Balmer on 19-03-2017.
 */

public interface AddGroupMemberMVP {


    public interface AddGroupMemberPresenter {

        List<User> getUsersMatchingString(String input);
        List<User> getAllUsers();
    }

        public interface AddGroupMemberView {


        }
}
