package com.oestjacobsen.android.get2gether.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Group extends RealmObject {

    @PrimaryKey
    private String mUUID;
    private String mGroupTitle;
    private String mGroupDesc;
    private RealmList<User> mParticipants;
    private boolean mActive;

    public Group() {
        mUUID = UUID.randomUUID().toString();
        mParticipants = new RealmList<>();
        mActive = false;
    }

    public String getUUID() {
        return mUUID;
    }


    public String getGroupTitle() {
        return mGroupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        mGroupTitle = groupTitle;
    }

    public String getGroupDesc() {
        return mGroupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        mGroupDesc = groupDesc;
    }

    public List<User> getParticipants() {
        return mParticipants;
    }

    public void addParticipant(User user) {
        mParticipants.add(user);
    }

    public void setParticipants(RealmList<User> participants) {
        mParticipants = participants;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }
}
