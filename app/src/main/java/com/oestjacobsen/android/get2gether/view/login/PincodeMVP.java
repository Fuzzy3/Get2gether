package com.oestjacobsen.android.get2gether.view.login;

import com.oestjacobsen.android.get2gether.model.User;


public interface PincodeMVP {
    public interface PincodePresenter {

        void authenticatePassword(String UUID, String password);

    }

    public interface PincodeView {
        void showToast(String msg);
        void passwordSuccesful();

    }



}
