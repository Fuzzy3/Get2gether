package com.oestjacobsen.android.get2gether.model;


import android.content.Context;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;

import java.util.ArrayList;
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
    public void addParticipantToGroup(User user, Group group) {
        mRealm.beginTransaction();
        group.addParticipant(user);
        mRealm.commitTransaction();
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
    public void updateOrAddGroup(Group group, String title, String description, List<User> participants) {
        final String fTitle = title;
        final String fDesc = description;
        final Group fGroup = group;

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fGroup.setGroupTitle(fTitle);
                fGroup.setGroupDesc(fDesc);
                mRealm.copyToRealmOrUpdate(fGroup);
            }
        });

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

    public void removeGroupFromUser(Group group, User user) {
        mRealm.beginTransaction();
        group.removeMember(user);
        user.removeGroup(group, user.getGroupHelper(group.getUUID()));
        mRealm.copyToRealmOrUpdate(group);
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
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
                    mRealm.copyToRealmOrUpdate(fGroupHelper);
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

    @Override
    public void addPendingGroupInvite(User user, Group group) {
        mRealm.beginTransaction();
        user.addPendingGroupInvite(group);
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
    }

    @Override
    public void addPendingGroup(User user, Group group) {
        mRealm.beginTransaction();
        user.removePendingGroup(group);
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
        addGroupToUser(group, user);
    }

    @Override
    public List<User> getParticipantsOfGroup(String uuid) {
        RealmResults<Group> result = mRealm.where(Group.class).equalTo("mUUID", uuid).findAll();
        if(result.size() > 0) {
            return result.get(0).getParticipants();
        }
        return new RealmList<User>();
    }

    @Override
    public Group getGroupFromUUID(String groupUIUD) {
        Group result = mRealm.where(Group.class).equalTo("mUUID", groupUIUD).findFirst();
        return result;
    }

    //TESTDATA
    private void addTestData() {
        //Users
        User u01 = new User();
        u01.setUsername("larstheman");
        u01.setFullName("Super man");
        u01.setPassword("1234");
        u01.setLatitude(55.715637);
        u01.setLongitude(12.476535);
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

    }

}
