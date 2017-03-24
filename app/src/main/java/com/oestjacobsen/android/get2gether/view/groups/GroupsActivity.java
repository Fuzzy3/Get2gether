package com.oestjacobsen.android.get2gether.view.groups;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupsActivity extends UserBaseActivity implements GroupsMVP.GroupsView{

    private final String TAG = UserBaseActivity.class.getSimpleName();

    @BindView(R.id.my_groups_toolbar) Toolbar mToolbar;
    @BindView(R.id.group_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.groups_create_new_group_button) Button mCreateGroupButton;
    @BindView(R.id.button_add_pending_group) Button mAddPendingGroupButton;

    private GroupListAdapter mAdapter;
    private List<Group> mGroupsAndPending;
    private GroupsMVP.GroupsPresenter mPresenter;
    private Group mSelectedGroup;
    private static final int COARSE_PERMISSION_REQUEST_CODE = 1111;
    private static final int FINE_PERMISSION_REQUEST_CODE = 2222;
    private int mPendingStarting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        mPresenter = new GroupsPresenterImpl(RealmDatabase.get(this), this);
        checkPermission();
        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "My Groups");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAddPendingGroupButton.setVisibility(View.INVISIBLE);
    }


    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new GroupListAdapter(mGroupsAndPending);
            mAdapter.setPendingStartingPos(mPendingStarting);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setGroupsList(mGroupsAndPending);
            mAdapter.setPendingStartingPos(mPendingStarting);
            mAdapter.notifyDataSetChanged();
        }
    }


    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, GroupsActivity.class);
        return i;
    }

    @OnClick(R.id.groups_create_new_group_button)
    public void onClickNewGroup() {
        startActivity(NewGroupActivity.newIntent(this));
    }

    @OnClick(R.id.button_add_pending_group)
    public void onClickAddPendingGroup() {
        mPresenter.addPendingGroup(mSelectedGroup);
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

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGroupsAndPending(List<Group> groupsAndPending, int pendingStartingPos) {
        mGroupsAndPending = groupsAndPending;
        mPendingStarting = pendingStartingPos;
        mSelectedGroup = null;
        mAddPendingGroupButton.setVisibility(View.INVISIBLE);
        updateUI();
    }

    //Adapter and viewholder for recyclerview
    public class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.groups_row_title) TextView mTitle;
        @BindView(R.id.group_active_button) Button mActiveButton;
        private Group mCurrentGroup;
        private boolean mIsPending;
        private int mPosition;


        public GroupHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindGroup(Group group, int position, boolean isPending) {
            mIsPending = isPending;
            mPosition = position;
            mTitle.setText(group.getGroupTitle());
            mActiveButton.setBackgroundColor(getActiveColor(group));
            mCurrentGroup = group;
            if(mIsPending) {
                if (mAdapter.getSelected_position() == mPosition) {
                    itemView.setBackgroundColor(ContextCompat.getColor(GroupsActivity.this, R.color.colorHighlight));
                    mSelectedGroup = group;
                } else {
                    itemView.setBackgroundColor(ContextCompat.getColor(GroupsActivity.this, R.color.colorPending));
                }
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }

        }

        private int getActiveColor(Group group) {
            if(mPresenter.isActive(group)) {
                Log.i(TAG, "GREEN");
                return Color.GREEN;
            } else {
                Log.i(TAG, "RED");
                return Color.RED;
            }
        }

        @OnClick(R.id.group_active_button)
        public void onActiveClick() {
            if (mIsPending) {
                mAdapter.notifyDataSetChanged();
                if (mPosition == mAdapter.getPrevious_position()) {
                    mAdapter.setSelected_position(-1);
                    mAdapter.setPrevious_position(-1);
                    mAddPendingGroupButton.setVisibility(View.INVISIBLE);

                } else {
                    mAdapter.setSelected_position(mPosition);
                    mAdapter.setPrevious_position(mPosition);
                    mAddPendingGroupButton.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            } else {
                Log.i(TAG, mCurrentGroup.getGroupTitle() + " Clicked");
                Log.i(TAG, mPresenter.isActive(mCurrentGroup) + ": state of active");
                mPresenter.setActive(mCurrentGroup, !mPresenter.isActive(mCurrentGroup));
                mPresenter.showActiveGroups();
                updateUI();
            }
        }

        @Override
        public void onClick(View view) {
            if (mIsPending) {
                mAdapter.notifyDataSetChanged();
                if (mPosition == mAdapter.getPrevious_position()) {
                    mAdapter.setSelected_position(-1);
                    mAdapter.setPrevious_position(-1);
                    mAddPendingGroupButton.setVisibility(View.INVISIBLE);

                } else {
                    mAdapter.setSelected_position(mPosition);
                    mAdapter.setPrevious_position(mPosition);
                    mAddPendingGroupButton.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            } else {
                startActivity(SelectedGroupActivity.newIntent(GroupsActivity.this, mSelectedGroup.getUUID()));
            }
        }
    }

    public class GroupListAdapter extends RecyclerView.Adapter<GroupHolder> {

        private List<Group> mGroups;
        private int mPendingStartingPos = 0;
        private int selected_position = -1;
        private int previous_position = -1;

        public GroupListAdapter(List<Group> groups) {
            mGroups = groups;
        }

        @Override
        public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.groups_list_row, parent, false);

            return new GroupHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupHolder holder, int position) {
            Group group = mGroups.get(position);
            if(position >= mPendingStartingPos) {
                holder.bindGroup(group,position, true);
            } else {
                holder.bindGroup(group, position, false);
            }
        }

        @Override
        public int getItemCount() {
            return mGroups.size();
        }

        public void setGroupsList(List<Group> groups) {
            mGroups = groups;
        }
        public void setPendingStartingPos(int startpos) {
            mPendingStartingPos = startpos;
        }

        public int getSelected_position() {
            return selected_position;
        }

        public void setSelected_position(int selected_position) {
            this.selected_position = selected_position;
        }

        public int getPrevious_position() {
            return previous_position;
        }

        public void setPrevious_position(int previous_position) {
            this.previous_position = previous_position;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case COARSE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getGroupsAndPending();
    }
}
