package com.oestjacobsen.android.get2gether;


import android.content.Context;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.FirebaseDB;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;

public class DatabasePicker {

    public static BaseDatabase getChosenDatabase(Context context) {

        return FirebaseDB.get();
    }
}
