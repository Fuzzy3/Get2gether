package com.oestjacobsen.android.get2gether.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID mUUID;
    private String mUsername;
    private String mFullName;
    private List<User> mFriends;
    private List<Group> mGroups;
    private List<User> mPendingInvites;
    private String mPassword;

    public User() {
        mUUID = UUID.randomUUID();
        mFriends = new ArrayList<>();
        mGroups = new ArrayList<>();
        mPendingInvites = new ArrayList<>();
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public List<User> getFriends() {
        return mFriends;
    }

    public void setFriends(List<User> friends) {
        mFriends = friends;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Group> groups) {
        mGroups = groups;
    }

    public List<User> getPendingInvites() {
        return mPendingInvites;
    }

    public void setPendingInvites(List<User> pendingInvites) {
        mPendingInvites = pendingInvites;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
