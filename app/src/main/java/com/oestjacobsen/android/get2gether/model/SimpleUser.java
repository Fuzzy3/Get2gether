package com.oestjacobsen.android.get2gether.model;

public class SimpleUser {

    private String UUID;
    private String FullName;

    public SimpleUser() {

    }

    public SimpleUser(String UUID, String fullName) {
        this.UUID = UUID;
        FullName = fullName;
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
}
