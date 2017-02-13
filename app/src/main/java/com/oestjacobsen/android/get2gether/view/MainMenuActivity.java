package com.oestjacobsen.android.get2gether.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.BinderThread;
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
import com.oestjacobsen.android.get2gether.view.friends.FriendsActivity;
import com.oestjacobsen.android.get2gether.view.groups.GroupsActivity;
import com.oestjacobsen.android.get2gether.view.profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends UserBaseActivity {

    String TAG = MainMenuActivity.class.getSimpleName();
    private AlphaAnimation imageButtonClickAnim = new AlphaAnimation(1F, 0.8F);

    @BindView(R.id.profile_button) ImageButton mProfileButton;
    @BindView(R.id.friends_button) ImageButton mFriendsButton;
    @BindView(R.id.my_groups_button) ImageButton mMyGroupsButton;
    @BindView(R.id.main_menu_toolbar) Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "");
        fitToScreen();
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MainMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
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

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_option:
            {
                Log.i(TAG, "HELP PRESSED");
                return true;
            }
            case R.id.about_option:
            {
                Log.i(TAG, "ABOUT PRESSED");
                return true;
            }
            case R.id.logoff_option:
            {
                Log.i(TAG, "LOG OFF PRESSED");
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    */

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
