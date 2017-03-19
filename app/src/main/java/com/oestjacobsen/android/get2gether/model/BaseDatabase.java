package com.oestjacobsen.android.get2gether.model;



import java.util.List;

public interface BaseDatabase {

    List<User> getAllUsers();
    User getUserFromUsername(String username);
    User getUserFromUUID(String UUID);
    void addUser(User user);
    void removeUser(User user);
    void addFriend(User user, User friend);
    void setActiveGroup(User user, Group group, boolean active);
    List<User> getUsersMatchingString(String input);

    void addGroupToUser(Group group, User user);

    void removeGroup(Group group);
    void updateOrAddGroup(Group group);




}
