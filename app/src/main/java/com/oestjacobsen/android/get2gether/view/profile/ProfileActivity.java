package com.oestjacobsen.android.get2gether.view.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends UserBaseActivity  {

    @BindView(R.id.profile_toolbar) Toolbar mToolbar;
    @BindView(R.id.profile_user) TextView mProfileName;
    @BindView(R.id.profile_username) TextView mProfileUsername;
    @BindView(R.id.profile_password) TextView mProfilePassword;

    private UserManager mSessionUser;
    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "Profile");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mSessionUser = UserManagerImpl.get();
        mCurrentUser = mSessionUser.getUser();

        mProfileName.setText(mCurrentUser.getFullName());
        mProfileUsername.setText(mCurrentUser.getUsername());
        mProfilePassword.setText(mCurrentUser.getPassword());
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, ProfileActivity.class);
        return i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.floating_button_editprofile)
    public void onClickEdit() {
        startActivity(EditProfileActivity.newIntent(this));
    }
}
