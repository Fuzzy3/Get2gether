package com.oestjacobsen.android.get2gether.model;



import java.util.List;

public interface BaseDatabase {

    List<User> getAllUsers();
    User getUserFromUsername(String username);
    User getUserFromUUID(String UUID);
    void addUser(User user);
    void removeUser(User user);
    void addFriend(User user, User friend);

    List<User> getUsersMatchingString(String input);

    void addGroup(Group group);
    void removeGroup(Group group);



}
