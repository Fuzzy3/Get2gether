package com.oestjacobsen.android.get2gether.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String mUUID;
    private String mUsername;
    private String mFullName;
    private RealmList<User> mFriends;
    private RealmList<Group> mGroups;
    private RealmList<User> mPendingInvites;
    private String mPassword;
    private double mLatitude;
    private double mLongitude;
    private RealmList<GroupIdHelperClass> mActiveGroups;

    public User() {
        mUUID = UUID.randomUUID().toString();
        mFriends = new RealmList<>();
        mGroups = new RealmList<>();
        mPendingInvites = new RealmList<>();
    }

    public String getUUID() {
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

    public void setFriends(RealmList<User> friends) {
        mFriends = friends;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(RealmList<Group> groups) {
        mGroups = groups;
    }

    public List<User> getPendingInvites() {
        return mPendingInvites;
    }

    public void setPendingInvites(RealmList<User> pendingInvites) {
        mPendingInvites = pendingInvites;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public void addFriend(User friend) {
        mFriends.add(friend);
    }

    public void removeFriend(User friend) {
        mFriends.remove(friend);
    }

    public void addGroup(Group group) {
        mGroups.add(group);
        mActiveGroups.add(new GroupIdHelperClass(group));
    }

    public RealmList<GroupIdHelperClass> getActiveGroups() {
        return mActiveGroups;
    }

    public void removeGroupFromActiveGroups(Group group) {
        GroupIdHelperClass removeGroup = null;

        for(GroupIdHelperClass id : mActiveGroups) {
            if(group.getUUID().equals(id.toString())) {
                removeGroup = id;
            }
        }
        if (removeGroup != null) {
            mActiveGroups.remove(removeGroup);
        }
    }

    public void addGroupToActiveGroups(Group group) {
        mActiveGroups.add(new GroupIdHelperClass(group));
    }

    public boolean checkActive(Group group) {
        for(GroupIdHelperClass id : mActiveGroups) {
            if (group.getUUID().equals(id.toString())) {
                return true;
            }
        }
        return false;
    }
}
