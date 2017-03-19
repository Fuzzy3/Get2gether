package com.oestjacobsen.android.get2gether.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupIdHelperClass extends RealmObject {

    @PrimaryKey
    private int id;
    private Group mGroup;


    public GroupIdHelperClass() {}

    public GroupIdHelperClass(Group group) {
        mGroup = group;
    }

    @Override
    public String toString() {
        return mGroup.getUUID();
    }
}
