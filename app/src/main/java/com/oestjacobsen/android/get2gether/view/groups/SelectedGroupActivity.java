package com.oestjacobsen.android.get2gether.view.groups;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.view.BaseActivity;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedGroupActivity extends UserBaseActivity  {

    private FragmentPagerAdapter mAdapter;
    @BindView(R.id.group_tablayout) TabLayout mTabs;
    @BindView(R.id.selected_group_viewpager) ViewPager mViewPager;
    @BindView(R.id.selected_group_toolbar) Toolbar mToolbar;
    private static String groupUUIDExtra = "GROUPUUIDEXTRA";
    private static String groupTitleExtra = "GROUPTITLEEXTRA";
    private String mCurrentGroupUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_group);
        mCurrentGroupUUID = getIntent().getStringExtra(groupUUIDExtra);
        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);

        if (mAdapter == null) {
            mAdapter = new SelectedGroupViewPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mAdapter);
            removeSlideToChangeTab(mViewPager);
            mTabs.setupWithViewPager(mViewPager);
        }

        setToolbar(mToolbar, getIntent().getStringExtra(groupTitleExtra));

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    private void removeSlideToChangeTab(ViewPager vp) {
        vp.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
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



    public static Intent newIntent(Context packageContext, String groupUUID, String groupTitle) {
        Intent i = new Intent(packageContext, SelectedGroupActivity.class);
        i.putExtra(groupUUIDExtra, groupUUID);
        i.putExtra(groupTitleExtra, groupTitle);
        return i;
    }

    public class SelectedGroupViewPagerAdapter extends FragmentPagerAdapter {
        private final int NUM_TABS = 4;
        private String mTabTitles[] = new String[]{ "Info", "Map", "Indoor", "Members" };

        public SelectedGroupViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SelectedGroupInfoFragment.newInstance(mCurrentGroupUUID);
                case 1:
                    return SelectedGroupMapFragment.newInstance();
                case 2:
                    return SelectedGroupIndoorFragment.newInstance();
                case 3:
                    return SelectedGroupMembersFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }
}
