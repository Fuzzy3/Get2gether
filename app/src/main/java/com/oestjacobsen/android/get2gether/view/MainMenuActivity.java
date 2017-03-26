package com.oestjacobsen.android.get2gether.view;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.services.LocationService;
import com.oestjacobsen.android.get2gether.view.friends.FriendsActivity;
import com.oestjacobsen.android.get2gether.view.groups.GroupsActivity;
import com.oestjacobsen.android.get2gether.view.profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends UserBaseActivity {

    String TAG = MainMenuActivity.class.getSimpleName();
    private AlphaAnimation imageButtonClickAnim = new AlphaAnimation(1F, 0.8F);
    private static final int COARSE_PERMISSION_REQUEST_CODE = 1111;
    private static final int FINE_PERMISSION_REQUEST_CODE = 2222;

    @BindView(R.id.profile_button) ImageButton mProfileButton;
    @BindView(R.id.friends_button) ImageButton mFriendsButton;
    @BindView(R.id.my_groups_button) ImageButton mMyGroupsButton;
    @BindView(R.id.main_menu_toolbar) Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        checkPermission();

        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "");
        fitToScreen();

        Intent i = new Intent(this, LocationService.class);
        startService(i);
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MainMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case COARSE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Permission denied do something
                }
                return;
            }
            case FINE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
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


    //Button interactions
    @OnClick(R.id.profile_button)
    public void onProfileClick() {
        mProfileButton.startAnimation(imageButtonClickAnim);
        startActivity(ProfileActivity.newIntent(this));

    }
    @OnClick(R.id.friends_button)
    public void onFriendsClicked() {
        mFriendsButton.startAnimation(imageButtonClickAnim);
        startActivity(FriendsActivity.newIntent(this));
        Log.i(TAG, "Friends Pressed");
    }

    @OnClick(R.id.my_groups_button)
    public void onMyGroupsClicked() {
        mMyGroupsButton.startAnimation(imageButtonClickAnim);
        startActivity(GroupsActivity.newIntent(this));
        Log.i(TAG, "My Groups Pressed");

    }



    private void fitToScreen() {
        //Get Screen size
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int size = Math.min(screenSize.x, screenSize.y);

        //Scale Buttons
        int profileButtonSize = Math.round(size * 0.3f);
        mProfileButton.setMaxWidth(profileButtonSize);
        mProfileButton.setMaxHeight(profileButtonSize);

        int friendsButtonSize = Math.round(size * 0.4f);
        mFriendsButton.setMaxWidth(friendsButtonSize);
        mFriendsButton.setMaxHeight(friendsButtonSize);

        int myGroupsButtonSize = Math.round(size * 0.5f);
        mMyGroupsButton.setMaxWidth(myGroupsButtonSize);
        mMyGroupsButton.setMaxHeight(myGroupsButtonSize);
    }

}
