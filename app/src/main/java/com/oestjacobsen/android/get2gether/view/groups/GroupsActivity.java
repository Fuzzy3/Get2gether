package com.oestjacobsen.android.get2gether.view.groups;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsActivity extends UserBaseActivity {

    @BindView(R.id.my_groups_toolbar) Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        setupView();
    }


    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "My Groups");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, GroupsActivity.class);
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


}
