package com.oestjacobsen.android.get2gether.view.login;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;

/**
 * Created by mr_oj on 16/03/2017.
 */

public class LoginPresenterImpl implements LoginPresenter {
    BaseDatabase mDatabase;

    public LoginPresenterImpl(BaseDatabase database) {
        mDatabase = database;
    }

    @Override
    public void authenticateUsername(String username) {
        mDatabase
    }
}
