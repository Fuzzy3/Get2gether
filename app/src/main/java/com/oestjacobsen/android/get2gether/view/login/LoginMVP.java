package com.oestjacobsen.android.get2gether.view.login;


import com.oestjacobsen.android.get2gether.model.User;

public interface LoginMVP {
    public interface LoginView {
        void usernameAcquired(String userUUID);
        void showToast(String msg);

    }

    public interface LoginPresenter {

        void authenticateUsername(String username);

    }

    public interface LoginModel {

    }

}
