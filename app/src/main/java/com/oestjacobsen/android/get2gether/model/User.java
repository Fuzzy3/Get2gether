package com.oestjacobsen.android.get2gether.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;

public class User extends RealmObject {

    private String mUUID;
    private String mUsername;
    private String mFullName;
    private RealmList<User> mFriends;
    private RealmList<Group> mGroups;
    private RealmList<User> mPendingInvites;
    private String mPassword;
    private double mLatitude;
    private double mLongitude;

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
}
