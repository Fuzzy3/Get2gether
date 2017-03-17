package com.oestjacobsen.android.get2gether.model;


import io.realm.OrderedRealmCollection;

public interface BaseDatabase {

    OrderedRealmCollection<User> getAllUsers();
    User getUser(String username);
    void addUser(User user);
    void removeUser(User user);

    void addGroup(Group group);
    void removeGroup(Group group);



}
