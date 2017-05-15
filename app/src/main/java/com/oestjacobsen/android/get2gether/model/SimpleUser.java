package com.oestjacobsen.android.get2gether.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class SimpleUser {

    private String UUID;
    private String FullName;
    private String IndoorLocation;

    public SimpleUser() {

    }

    public SimpleUser(String UUID, String fullName, String indoorLocation) {
        this.UUID = UUID;
        FullName = fullName;
        IndoorLocation = indoorLocation;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getIndoorLocation() {
        return IndoorLocation;
    }

    public void setIndoorLocation(String indoorLocation) {
        IndoorLocation = indoorLocation;
    }

}
