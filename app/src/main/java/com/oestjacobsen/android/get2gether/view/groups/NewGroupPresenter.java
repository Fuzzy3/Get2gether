package com.oestjacobsen.android.get2gether.view.groups;


import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;

public interface NewGroupPresenter {


    List<User> getUsersMatchingString(String input);


    List<User> getAllUsers();

}
