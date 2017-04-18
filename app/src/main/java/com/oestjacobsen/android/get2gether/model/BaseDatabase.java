package com.oestjacobsen.android.get2gether.model;



import android.location.Location;

import com.facebook.AccessToken;

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
    void updateOrAddGroup(Group group, String title, String description, List<User> participants);
    void addPendingInvite(User user, User friend);
    void addPendingFriend(User user, User friend);
    void addPendingGroupInvite(User user, Group group);
    void removeGroupFromUser(Group group, User user);
    void addParticipantToGroup(User user, Group group);

    //MAYBE TO BE DELETED
    void setLoginCallback(loginCallback loginCB);
    void setupDatabaseSync();

    public interface loginCallback {
        void loginSucceded();
    }



    List<User> getParticipantsOfGroup(String uuid);

    void addPendingGroup(User mCurrentUser, Group group);

    Group getGroupFromUUID(String groupUIUD);

    void updateUserPosition(User user, Location location);

    void updateUserIndoorPosition(User mCurrentUser, String s);

}
