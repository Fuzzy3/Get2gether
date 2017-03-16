package com.oestjacobsen.android.get2gether.view.friends;

import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;

import io.realm.OrderedRealmCollection;


public interface FriendsPresenter {

    List<User> getFriends();
}
