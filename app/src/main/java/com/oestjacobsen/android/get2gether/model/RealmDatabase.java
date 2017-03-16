package com.oestjacobsen.android.get2gether.model;


import android.content.Context;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmDatabase implements BaseDatabase {

    private static Realm mRealm;
    private static RealmDatabase mRealmDatabase;

    public RealmDatabase() {
        addTestData();
    }

    public static RealmDatabase get(Context context) {
        if (mRealmDatabase == null) {
            Realm.init(context);
            mRealm = Realm.getDefaultInstance();
            mRealmDatabase = new RealmDatabase();
        }
        return mRealmDatabase;
    }

    public OrderedRealmCollection<User> getAllUsers() {
        return mRealm.where(User.class).findAll();
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


    private void addTestData() {
        User u01 = new User();
        u01.setUsername("larstheman");
        u01.setFullName("Super man");
        u01.setPassword("1234");
        addUser(u01);

        User u02 = new User();
        u02.setUsername("hanzi");
        u02.setFullName("Spider man");
        u02.setPassword("2222");
        addUser(u02);

        User u03 = new User();
        u03.setUsername("princess_line");
        u03.setFullName("Habitus");
        u03.setPassword("9898");
        addUser(u03);

        User u04 = new User();
        u04.setUsername("Admin");
        u04.setFullName("Søren Oest Jacobsen");
        u04.setPassword("1551");
        addUser(u04);

        User u05 = new User();
        u05.setUsername("das_robot");
        u05.setFullName("Batman");
        u05.setPassword("1111");
        addUser(u05);
    }

}
