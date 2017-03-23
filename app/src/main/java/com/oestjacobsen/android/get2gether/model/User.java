package com.oestjacobsen.android.get2gether.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject  {

    @PrimaryKey
    private String mUUID;
    private String mUsername;
    private String mFullName;
    private RealmList<User> mFriends;
    private RealmList<Group> mGroups;
    private RealmList<User> mPendingInvites;
    private RealmList<Group> mPendingGroupInvites;
    private String mPassword;
    private double mLatitude;
    private double mLongitude;
    private RealmList<GroupIdHelperClass> mActiveGroups;

    public User() {
        mUUID = UUID.randomUUID().toString();
        mFriends = new RealmList<>();
        mGroups = new RealmList<>();
        mPendingInvites = new RealmList<>();
        mActiveGroups = new RealmList<>();
    }

    public String getUUID() {
        return mUUID;
    }

    public RealmList<Group> getPendingGroupInvites() {
        return mPendingGroupInvites;
    }

    public void setPendingGroupInvites(RealmList<Group> mPendingGroupInvites) {
        this.mPendingGroupInvites = mPendingGroupInvites;
    }

    public void addPendingGroupInvite(Group group) {
        mPendingGroupInvites.add(group);
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

    public void addPendingInvite(User user) {
        mPendingInvites.add(user);
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

    public void addGroup(Group group, GroupIdHelperClass groupHelper) {
        mGroups.add(group);
        mActiveGroups.add(groupHelper);
    }

    public RealmList<GroupIdHelperClass> getActiveGroups() {
        return mActiveGroups;
    }

    public GroupIdHelperClass getGroupHelper(String mUUID) {
        for(GroupIdHelperClass groupHelper : mActiveGroups) {
            if (groupHelper.getGroupUUID().equals(mUUID)) {
                return groupHelper;
            }
        }
        return null;
    }

    public boolean checkActive(Group group) {
        for(GroupIdHelperClass id : mActiveGroups) {
            if (group.getUUID().equals(id.getGroupUUID())) {
                return true;
            }
        }
        return false;
    }


    public void removePendingFriend(User friend) {
        RealmList<User> newPendingList = new RealmList<>();
        for(User pending : mPendingInvites) {
            if(!pending.getUUID().equals(friend.getUUID())) {
                newPendingList.add(friend);
            }
        }

        mPendingInvites = newPendingList;
    }

    public void removePendingGroup(Group group) {
        RealmList<Group> newPendingList = new RealmList<>();
        for(Group pending : mPendingGroupInvites) {
            if(!pending.getUUID().equals(group.getUUID())) {
                newPendingList.add(pending);
            }
        }

        mPendingGroupInvites = newPendingList;
    }
}
