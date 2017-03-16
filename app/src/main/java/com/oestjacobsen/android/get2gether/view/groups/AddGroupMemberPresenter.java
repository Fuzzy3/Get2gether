package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;

/**
 * Created by mr_oj on 16/03/2017.
 */

public interface AddGroupMemberPresenter {


    List<User> getUsersMatchingString(String input);


    List<User> getAllUsers();
}
