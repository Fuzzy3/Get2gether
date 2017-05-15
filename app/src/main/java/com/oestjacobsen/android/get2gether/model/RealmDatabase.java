package com.oestjacobsen.android.get2gether.model;


import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Realm.Transaction.Callback;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RealmDatabase implements BaseDatabase {


    private static final String TAG = "RealmDatabase";
    private static Realm mRealm;
    private static RealmDatabase mRealmDatabase;



    private RealmDatabase() {
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
                User user = mRealm.where(User.class).equalTo("mUUID", fUser.getUUID()).findFirst();
                user.deleteFromRealm();
            }
        });
    }

    @Override
    public void addFriend(User user, User friend) {
        final User fUser = user;
        final User fFriend = friend;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fUser.addFriend(fFriend);
                mRealm.copyToRealmOrUpdate(fUser);
            }
        });
    }


    /*
    @Override
    public void addFriend(User user, User friend) {
        OrderedRealmCollection<User> findUser = mRealm.where(User.class).equalTo("mUUID", user.getUUID()).findAll();
        final User fUser = findUser.first();

        OrderedRealmCollection<User> findFriend = mRealm.where(User.class).equalTo("mUUID", friend.getUUID()).findAll();
        final User fFriend = findFriend.first();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fUser.addFriend(fFriend);
                mRealm.copyToRealmOrUpdate(fUser);
            }
        });
    }*/


    @Override
    public List<User> getUsersMatchingString(String input) {
        final String fInput = input;
        OrderedRealmCollection<User> results =
                mRealm.where(User.class).beginsWith("mFullName", fInput).findAll();
        return results;
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
        final User fUser = user;
        final Group fGroup = group;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fGroup.addParticipant(fUser);
            }
        });
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
                Group removeGroup = mRealm.where(Group.class)
                        .equalTo("mStringUUID", fGroup.getUUID()).findFirst();
                if(removeGroup != null) {
                    removeGroup.deleteFromRealm();
                }
            }
        });
    }

    public void removeGroupFromUser(Group group, User user) {
        final Group fGroup = group;
        final User fUser = user;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fGroup.removeMember(fUser);
                fUser.removeGroup(fGroup, fUser.getGroupHelper(fGroup.getUUID()));
                mRealm.copyToRealmOrUpdate(fGroup);
                mRealm.copyToRealmOrUpdate(fUser);
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
                    mRealm.copyToRealmOrUpdate(fGroupHelper);
                    fUser.addGroup(fGroup,fGroupHelper);
                    mRealm.copyToRealmOrUpdate(fUser);
                }
            });
        }
    }

    @Override
    public void addPendingInvite(User user, User friend) {
        final User fUser = mRealm.where(User.class).equalTo("mUUID", user.getUUID()).findFirst();
        final User fFriend = mRealm.where(User.class).equalTo("mUUID", friend.getUUID()).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fFriend.addPendingInvite(fUser);
                mRealm.copyToRealmOrUpdate(fFriend);
            }
        });
    }

    /*@Override
    public void addPendingInvite(User user, User friend) {
        Realm realm = Realm.getDefaultInstance();
        OrderedRealmCollection<User> findUser = mRealm.where(User.class).equalTo("mUUID", user.getUUID()).findAll();
        final User fUser = findUser.first();

        OrderedRealmCollection<User> findFriend = mRealm.where(User.class).equalTo("mUUID", friend.getUUID()).findAll();
        final User fFriend = findFriend.first();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fFriend.addPendingInvite(fUser);
                mRealm.copyToRealmOrUpdate(fFriend);
            }
        });
    }*/

    @Override
    public void addPendingFriend(User user, User friend) {
        final User fUser = user;
        final User fFriend = friend;

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fUser.removePendingFriend(fFriend);
                fUser.addFriend(fFriend);
                fFriend.removePendingFriend(fUser);
                fFriend.addFriend(fUser);
                mRealm.copyToRealmOrUpdate(fUser);
                mRealm.copyToRealmOrUpdate(fFriend);

            }
        });
    }

    @Override
    public void addPendingGroupInvite(User user, Group group) {
        final User fUser = user;
        final Group fGroup = group;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fUser.addPendingGroupInvite(fGroup);
                mRealm.copyToRealmOrUpdate(fUser);
            }
        });
    }

    @Override
    public void addPendingGroup(User user, Group group) {
        final User fUser = user;
        final Group fGroup = group;

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fUser.removePendingGroup(fGroup);
                mRealm.copyToRealmOrUpdate(fUser);
                GroupIdHelperClass groupHelper = fUser.getGroupHelper(fGroup.getUUID());
                if(groupHelper == null) {
                    final GroupIdHelperClass fGroupHelper = new GroupIdHelperClass(fGroup);
                    mRealm.copyToRealmOrUpdate(fGroupHelper);
                    fUser.addGroup(fGroup,fGroupHelper);
                    mRealm.copyToRealmOrUpdate(fUser);
                }
            }
        });
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

    @Override
    public void updateUserPosition(String uuid, Location location, Realm realm) {
        final User fUser = mRealm.where(User.class).equalTo("mUUID", uuid).findFirst();
        final Location fLocation = location;
        Log.i(TAG, realm.toString());
        if(realm == null) {
            if (fUser != null) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        fUser.setLatitude(fLocation.getLatitude());
                        fUser.setLongitude(fLocation.getLongitude());
                        mRealm.copyToRealmOrUpdate(fUser);
                    }
                });
            }
        } else {
            if (fUser != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        fUser.setLatitude(fLocation.getLatitude());
                        fUser.setLongitude(fLocation.getLongitude());
                        realm.copyToRealmOrUpdate(fUser);
                    }
                });
            }
        }
    }

    @Override
    public void updateUserIndoorPosition(String uuid, final String indoorLocation, Realm realm) {
        final User fUser = mRealm.where(User.class).equalTo("mUUID", uuid).findFirst();

        if(realm == null) {
            if((fUser != null) && (indoorLocation != null)) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        fUser.setIndoorLocation(indoorLocation);
                        mRealm.copyToRealmOrUpdate(fUser);
                    }
                });
            }
        } else {
            if((fUser != null) && (indoorLocation != null)) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        fUser.setIndoorLocation(indoorLocation);
                        realm.copyToRealmOrUpdate(fUser);
                    }
                });
            }
        }
    }

    //TESTDATA
    //Not used after facebooklogin was implemented
    private void addTestData() {
        //Users
        /*User u01 = new User();
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
        addUser(u05);*/

    }

}
