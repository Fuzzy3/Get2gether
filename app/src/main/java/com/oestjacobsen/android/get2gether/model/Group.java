package com.oestjacobsen.android.get2gether.model;


import java.util.List;
import java.util.UUID;

public class Group {

    private UUID mUUID;
    private String mGroupTitle;
    private String mGroupDesc;
    private List<User> mParticipants;
    private boolean mActive;

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
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

    public void setParticipants(List<User> participants) {
        mParticipants = participants;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }
}
