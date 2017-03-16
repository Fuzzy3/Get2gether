package com.oestjacobsen.android.get2gether.model;


import io.realm.OrderedRealmCollection;

public interface BaseDatabase {

    OrderedRealmCollection<User> getAllUsers();
    void addUser(User user);
    void removeUser(User user);

}
