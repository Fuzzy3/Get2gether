package com.oestjacobsen.android.get2gether.model;


import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupIdHelperClass extends RealmObject {

    @PrimaryKey
    private String mUUID;
    private boolean mActive = true;
    private Group mGroup;


    public GroupIdHelperClass() {
        mUUID = UUID.randomUUID().toString();

    }

    public GroupIdHelperClass(Group group) {
        mGroup = group;
        mUUID = UUID.randomUUID().toString();

    }

    public String getGroupUUID() {
        return mGroup.getUUID();
    }

    public String getUUID() {
        return mUUID;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }
}
