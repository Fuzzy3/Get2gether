package com.oestjacobsen.android.get2gether.view.login;


import android.os.Bundle;

import com.facebook.AccessToken;
import com.oestjacobsen.android.get2gether.model.User;

public interface LoginMVP {
    interface LoginView {
        void usernameAcquired(String userUUID);
        void showToast(String msg);

    }

    interface LoginPresenter {

        void authenticateUsername(String username);

        void authenticateFacebook(AccessToken accessToken, Bundle facebookData);


    }

}
