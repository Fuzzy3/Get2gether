package com.oestjacobsen.android.get2gether.model;



import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseDB implements BaseDatabase {

    private static final String TAG = "FirebaseDB";
    private static FirebaseDB mFirebaseDB;
    private static FirebaseDatabase mFirebase;
    private static DatabaseReference mFirebaseDatabase;
    private loginCallback mLoginCallback;


    private FirebaseDB() {

    }

    public static FirebaseDB get() {
        if (mFirebaseDatabase == null) {
            mFirebaseDB = new FirebaseDB();
            mFirebase = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebase.getReference();
        }
        return mFirebaseDB;
    }


    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserFromUsername(String username) {
        return null;
    }

    @Override
    public User getUserFromUUID(String UUID) {
        DatabaseReference usersRef = mFirebaseDatabase.child("users");


        usersRef.child(UUID + "01").setValue(new SimpleUser(UUID + "01", "Søren Oest Jacobsen", "5A12"));
        usersRef.child(UUID + "02").setValue(new SimpleUser(UUID + "02", "Hans Hansen", "3A54"));

        /*
        Map<String, SimpleUser> users = new HashMap<>();

        SimpleUser user01 = new SimpleUser(UUID + "1", "Søren Oest Jacobsen", "5A12");
        users.put(user01.getUUID(), user01);

        SimpleUser user02 = new SimpleUser(UUID + "2", "Hans Hansen", "3A54");
        users.put(user02.getUUID(), user02);

        usersRef.setValue(users);
        */


        /*Query userQuery = ref.equalTo(UUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, dataSnapshot.getChildrenCount() + "");
                Log.i(TAG, "VALUE: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        return null;
    }

    @Override
    public void addUser(User user) {
        DatabaseReference usersRef = mFirebaseDatabase.child("users");

        Map<String, User> users = new HashMap<>();
        users.put(user.getUUID(), user);

        usersRef.setValue(users);
    }



    @Override
    public void removeUser(User user) {

    }

    @Override
    public void addFriend(User user, User friend) {

    }

    @Override
    public void setActiveGroup(User user, Group group, boolean active) {

    }

    @Override
    public List<User> getUsersMatchingString(String input) {
        return null;
    }

    @Override
    public void addGroupToUser(Group group, User user) {

    }

    @Override
    public void removeGroup(Group group) {

    }

    @Override
    public void updateOrAddGroup(Group group, String title, String description, List<User> participants) {

    }

    @Override
    public void addPendingInvite(User user, User friend) {

    }

    @Override
    public void addPendingFriend(User user, User friend) {

    }

    @Override
    public void addPendingGroupInvite(User user, Group group) {

    }

    @Override
    public void removeGroupFromUser(Group group, User user) {

    }

    @Override
    public void addParticipantToGroup(User user, Group group) {

    }

    /*@Override
    public void setLoginCallback(loginCallback loginCB) {
        mLoginCallback = loginCB;
    }


    @Override
    public void setupDatabaseSync() {
        //Setup authenticate process
        mLoginCallback.loginSucceded();
    }*/

    @Override
    public List<User> getParticipantsOfGroup(String uuid) {
        return null;
    }

    @Override
    public void addPendingGroup(User mCurrentUser, Group group) {

    }

    @Override
    public Group getGroupFromUUID(String groupUIUD) {
        return null;
    }

    @Override
    public void updateUserPosition(User user, Location location) {

    }

    @Override
    public void updateUserIndoorPosition(User mCurrentUser, String s) {

    }
}
