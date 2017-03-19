package com.oestjacobsen.android.get2gether.view.login;

import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;

public class PincodePresenterImpl implements PincodeMVP.PincodePresenter {

    private BaseDatabase mDatabase;
    private PincodeMVP.PincodeView mView;
    private UserManager mUserManager;

    public PincodePresenterImpl(BaseDatabase database, PincodeMVP.PincodeView view) {
        mDatabase = database;
        mView = view;
        mUserManager = UserManagerImpl.get();
    }

    @Override
    public void authenticatePassword(String uuid, String password) {
        User user = mDatabase.getUserFromUUID(uuid);
        if (user != null) {
            if(user.getPassword().equals(password)) {
                mUserManager.setCurrentUser(user);
                mView.passwordSuccesful();
            } else {
                mView.showToast("Wrong password");
            }
        } else {
            mView.showToast("You are logged out");
        }
    }

}
