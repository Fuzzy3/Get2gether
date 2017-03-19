package com.oestjacobsen.android.get2gether.view.groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
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
import com.oestjacobsen.android.get2gether.model.TestData;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;
import com.oestjacobsen.android.get2gether.view.friends.AddFriendActivity;
import com.oestjacobsen.android.get2gether.view.friends.FriendsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupsActivity extends UserBaseActivity implements GroupsMVP.GroupsView{

    private final String TAG = UserBaseActivity.class.getSimpleName();

    @BindView(R.id.my_groups_toolbar) Toolbar mToolbar;
    @BindView(R.id.group_list_recyclerview) RecyclerView mRecyclerView;
    private GroupListAdapter mAdapter;
    private List<Group> mGroupList;
    private GroupsMVP.GroupsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        mPresenter = new GroupsPresenterImpl(RealmDatabase.get(this), this);
        setupView();
        updateUI();
    }

    private void updateUI() {
        mGroupList = mPresenter.getGroups();

        if (mAdapter == null) {
            mAdapter = new GroupListAdapter(mGroupList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "My Groups");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }



    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, GroupsActivity.class);
        return i;
    }

    @OnClick(R.id.floating_button_new_group)
    public void onClickNewGroup() {
        startActivity(NewGroupActivity.newIntent(this));
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

    //Adapter and viewholder for recyclerview
    public class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.groups_row_title) TextView mTitle;
        @BindView(R.id.group_active_button) Button mActiveButton;
        private Group mCurrentGroup;

        public GroupHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindGroup(Group group) {
            mTitle.setText(group.getGroupTitle());
            mActiveButton.setBackgroundColor(getActiveColor(group));
            mCurrentGroup = group;
        }

        private int getActiveColor(Group group) {
            if(mPresenter.getActive(group)) {
                Log.i(TAG, "GREEN");
                return Color.GREEN;
            } else {
                Log.i(TAG, "RED");
                return Color.RED;
            }
        }

        @OnClick(R.id.group_active_button)
        public void onActiveClick() {
            Log.i(TAG, mCurrentGroup.getGroupTitle() + " Clicked");
            Log.i(TAG, mPresenter.getActive(mCurrentGroup) + ": state of active");
            mPresenter.setActive(mCurrentGroup, !mPresenter.getActive(mCurrentGroup));
            mPresenter.showActiveGroups();
            updateUI();
        }

        @Override
        public void onClick(View view) {
            startActivity(SelectedGroupActivity.newIntent(GroupsActivity.this));
        }
    }

    public class GroupListAdapter extends RecyclerView.Adapter<GroupHolder> {

        private List<Group> mGroups;

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
            holder.bindGroup(group);
        }

        @Override
        public int getItemCount() {
            return mGroups.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
