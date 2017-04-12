package com.oestjacobsen.android.get2gether.view.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.oestjacobsen.android.get2gether.FacebookAuth;
import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.services.LocationService;
import com.oestjacobsen.android.get2gether.view.BaseActivity;
import com.oestjacobsen.android.get2gether.view.MainMenuActivity;

import org.json.JSONException;
import org.json.JSONObject;




import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements LoginMVP.LoginView {

    private static final int COARSE_PERMISSION_REQUEST_CODE = 1111;
    private static final int FINE_PERMISSION_REQUEST_CODE = 2222;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 3333;
    private static final int BLUETOOTHADMIN_PERMISSION_REQUEST_CODE = 4444;

    private String TAG = MainActivity.class.getSimpleName();
    private LoginMVP.LoginPresenter mPresenter;

    private CallbackManager mCallbackManager;
    private FacebookAuth mFacebookAuth;
    private ProgressDialog mProgressDialog;
    private AccessToken mAccessToken;


    private static final String HOST_ITU = "130.226.142.162";
    private static final String HOST_LOCAL = "SOeXPS";
    private static final String HOST= HOST_ITU;
    private static final String AUTH_URL = "http://" + HOST + ":9080/auth";
    private static final int INTERNET_PERMISSION_REQUEST_CODE = 3333;


    @BindView(R.id.login_facebook_button) LoginButton mFacebookButton;
    @BindView(R.id.login_toolbar) Toolbar mToolbar;
    @BindView(R.id.username_edit_text) EditText mUsernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        mPresenter = new LoginPresenterImpl(RealmDatabase.get(this), this);
        if(isLoggedIn()) {
            //Jump right into app.
        }

        setupView();

        checkPermission();

        mFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog = new ProgressDialog(MainActivity.this);
                mProgressDialog.show();
            }
        });
        setupFacebookAuth();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void  setupFacebookAuth() {
        mFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "User id: " + loginResult.getAccessToken().getUserId());
                Log.i(TAG, "AuthToken: " + loginResult.getAccessToken().getToken());
                mAccessToken = loginResult.getAccessToken();
                loginComplete();

            }

            @Override
            public void onCancel() {
                Log.i(TAG, "FacebookLogin cancelled");
                mProgressDialog.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "There was an error, incorrect password maybe \nError: " + error.getMessage());
                mProgressDialog.dismiss();
            }
        });
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
    }

    @OnClick(R.id.continue_button_login)
    public void onClick(){
        String username = mUsernameInput.getText().toString();
        mPresenter.authenticateUsername(username);
    }

    private void setupView(){
        ButterKnife.bind(this);

        if(mUsernameInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void usernameAcquired(String userUUID) {
        startActivity(MainMenuActivity.newIntent(this));
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    void loginComplete() {
        Log.i(TAG, "Login complete, time to collect data");
        //mProgressDialog = new ProgressDialog(MainActivity.this);
        //mProgressDialog.setMessage("WAITING FOR FACEBOOK :D");
        //mProgressDialog.show();
        Log.i("accessToken", mAccessToken.getToken());

        final GraphRequest request = GraphRequest.newMeRequest(mAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("LoginActivity", response.toString());
                // Get facebook data from login
                Bundle facebookData = getFacebookData(object);
                while(facebookData.getString("idFacebook") == null) {
                    facebookData = getFacebookData(object);
                }
                mPresenter.authenticateFacebook(mAccessToken, facebookData);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            //Collect facebook profile image - Not used in app
            /*try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }*/

            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
            return null;
        }
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        mAccessToken = accessToken;
        return accessToken != null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BLUETOOTH_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Permission denied do something
                }
                return;
            }
            case BLUETOOTHADMIN_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Permission denied do something
                }
                return;
            }
            case COARSE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Permission denied do something
                }
                return;
            }
            case FINE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Permission denied do something
                }
                return;
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //int fineLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            //int coarseLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH},
                        BLUETOOTH_PERMISSION_REQUEST_CODE);

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        BLUETOOTHADMIN_PERMISSION_REQUEST_CODE);

            }


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_PERMISSION_REQUEST_CODE);

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        COARSE_PERMISSION_REQUEST_CODE);

            }
        }

    }
}
