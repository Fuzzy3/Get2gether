package com.oestjacobsen.android.get2gether.view.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends UserBaseActivity implements EditProfileMVP.EditProfileView {

    @BindView(R.id.edit_profile_toolbar) Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "Edit Profile");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, EditProfileActivity.class);
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

    @OnClick(R.id.floating_button_acceptprofile)
    public void onClickEdit() {
        startActivity(ProfileActivity.newIntent(this));
    }
}
