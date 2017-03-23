package com.oestjacobsen.android.get2gether.model;


import android.content.Context;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RealmDatabase implements BaseDatabase {

    private static Realm mRealm;
    private static RealmDatabase mRealmDatabase;


    public RealmDatabase() {
        RealmResults results = mRealm.where(User.class).findAll();
        if(results.size() == 0) {
            addTestData();
        }
    }

    public static RealmDatabase get(Context context) {
        if (mRealmDatabase == null) {
            Realm.init(context);
            mRealm = Realm.getDefaultInstance();
            mRealmDatabase = new RealmDatabase();
        }
        return mRealmDatabase;
    }

    //----------USER FUNCTIONS------------
    public List<User> getAllUsers() {
        OrderedRealmCollection results =  mRealm.where(User.class).findAll();
        return mRealm.copyFromRealm(results);
    }

    public void addUser(User user) {
        final User fUser = user;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealm(fUser);
            }
        });
    }


    public void removeUser(User user) {
        final User fUser = user;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<User> rows = mRealm.where(User.class).equalTo("mStringUUID", fUser.getUUID()).findAll();
                if (rows.size() > 0) {
                    rows.get(0).deleteFromRealm();
                }
            }
        });
    }

    @Override
    public void addFriend(User user, User friend) {
        mRealm.beginTransaction();
        user.addFriend(friend);
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
    }

    @Override
    public List<User> getUsersMatchingString(String input) {
        final String fInput = input;
        OrderedRealmCollection<User> results = mRealm.where(User.class).beginsWith("mFullName", fInput).findAll();
        return mRealm.copyFromRealm(results);
    }


    public User getUserFromUsername(String username) {
        final String fUsername = username;
        RealmResults<User> rows = mRealm.where(User.class).equalTo("mUsername", fUsername).findAll();
        if (rows.size() > 0) {
            return rows.get(0);
        }
        return null;
    }

    public User getUserFromUUID(String UUID) {
        final String fUUID = UUID;
        RealmResults<User> rows = mRealm.where(User.class).equalTo("mUUID", fUUID).findAll();
        if (rows.size() > 0) {
            return rows.get(0);
        }
        return null;
    }

    @Override
    public void setActiveGroup(User user, final Group group, final boolean active) {
        final User fUser = user;
        final Group fGroup = group;
        final GroupIdHelperClass fGroupHelper = fUser.getGroupHelper(fGroup.getUUID());
        if (fGroupHelper != null) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                        fGroupHelper.setActive(active);
                }
            });
        }
    }

    //------------GROUP FUNCTIONS--------------
    //If no such group exists, it creates one
    public void updateOrAddGroup(Group group) {
        final Group fGroup = group;
        final RealmResults<Group> groupAlreadyExist = mRealm.where(Group.class).equalTo("mUUID", fGroup.getUUID()).findAll();

        //Group already exists and we just need to update data
        if(groupAlreadyExist.size() > 0) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    groupAlreadyExist.get(0).setGroupTitle(fGroup.getGroupTitle());
                    groupAlreadyExist.get(0).setGroupDesc(fGroup.getGroupDesc());
                    groupAlreadyExist.get(0).setParticipants(fGroup.getParticipants());
                }
            });
        }
        //Group doesn't exist and we need to create a new object in the database
        else {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mRealm.copyToRealm(fGroup);
                }
            });
        }

    }

    public void removeGroup(Group group) {
        final Group fGroup = group;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Group> rows = mRealm.where(Group.class).equalTo("mStringUUID", fGroup.getUUID()).findAll();
                if(rows.size() > 0) {
                    rows.get(0).deleteFromRealm();
                }
            }
        });
    }

    @Override
    public void addGroupToUser(Group group, User user) {
        final Group fGroup = group;
        final User fUser = user;
        GroupIdHelperClass groupHelper = fUser.getGroupHelper(fGroup.getUUID());
        if(groupHelper == null) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final GroupIdHelperClass fGroupHelper = new GroupIdHelperClass(fGroup);
                    mRealm.copyToRealm(fGroupHelper);
                    fUser.addGroup(fGroup,fGroupHelper);
                }
            });
        }
    }

    @Override
    public void addPendingInvite(User user, User friend) {
        mRealm.beginTransaction();
        friend.addPendingInvite(user);
        mRealm.copyToRealmOrUpdate(friend);
        mRealm.commitTransaction();
    }

    @Override
    public void addPendingFriend(User user, User friend) {
        mRealm.beginTransaction();
        user.removePendingFriend(friend);
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
        addFriend(friend, user);
        addFriend(user, friend);

    }

    //TESTDATA
    private void addTestData() {
        //Users
        User u01 = new User();
        u01.setUsername("larstheman");
        u01.setFullName("Super man");
        u01.setPassword("1234");
        addUser(u01);

        User u02 = new User();
        u02.setUsername("hanzi");
        u02.setFullName("Spider man");
        u02.setPassword("1234");
        addUser(u02);

        User u03 = new User();
        u03.setUsername("princess_line");
        u03.setFullName("Habitus");
        u03.setPassword("1234");
        addUser(u03);

        User u04 = new User();
        u04.setUsername("admin");
        u04.setFullName("Søren Oest Jacobsen");
        u04.setPassword("1234");
        addUser(u04);

        User u05 = new User();
        u05.setUsername("das_robot");
        u05.setFullName("Batman");
        u05.setPassword("1234");
        addUser(u05);


        //Groups
        Group g01 = new Group();
        g01.setGroupTitle("Home");
        g01.setGroupDesc("Class trip to the zoo");
        updateOrAddGroup(g01);

        Group g02 = new Group();
        g02.setGroupTitle("The Beach");
        g02.setGroupDesc("The friends are going to a Concert!");
        updateOrAddGroup(g02);

        Group g03 = new Group();
        g03.setGroupTitle("London");
        g03.setGroupDesc("Group to check up on students");
        updateOrAddGroup(g03);

        Group g04 = new Group();
        g04.setGroupTitle("School Party");
        g04.setGroupDesc("Group for the awesome school group");
        updateOrAddGroup(g04);
    }

}
