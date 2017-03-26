package com.oestjacobsen.android.get2gether;

import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public abstract class FacebookAuth {
    private final LoginButton mLoginButton;
    private final CallbackManager mCallbackManager;

    public FacebookAuth(final LoginButton loginButton) {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = loginButton;

        mLoginButton.setReadPermissions("email");

        //CallbackRegistration
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onRegistrationComplete(loginResult);
            }

            @Override
            public void onCancel() {
                onAuthCancelled();
            }

            @Override
            public void onError(FacebookException error) {
                onAuthError();
            }
        });
    }

    /**
     * Called if the authentication is cancelled by the user.
     *
     * Adapter method, developer might want to override this method  to provide
     * custom logic.
     */
    public void onAuthCancelled() {}

    /**
     * Called if the authentication fails.
     *
     * Adapter method, developer might want to override this method  to provide
     * custom logic.
     */
    public void onAuthError () {}

    /**
     * Notify this class about the {@link FragmentActivity#onResume()} event.
     */
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Called once we obtain a token from Facebook API.
     * @param loginResult contains the token obtained from Facebook API.
     */
    public abstract void onRegistrationComplete(final LoginResult loginResult);





}
