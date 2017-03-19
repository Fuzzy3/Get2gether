package com.oestjacobsen.android.get2gether.view.groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;
import com.oestjacobsen.android.get2gether.view.friends.AddFriendActivity;
import com.oestjacobsen.android.get2gether.view.friends.FriendsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGroupActivity extends UserBaseActivity implements NewGroupMVP.NewGroupView{

    @BindView(R.id.new_group_name_edit_text)
    EditText mNameEditText;
    @BindView(R.id.new_group_about_edit_text)
    EditText mAboutEditText;
    @BindView(R.id.new_group_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.new_group_show_members_recyclerview) RecyclerView mRecyclerView;

    private NewGroupMVP.NewGroupPresenter mPresenter;
    private User mSelectedUser;
    private Group mCurrentGroup;
    private NewGroupAdapter mAdapter;
    private List<User> mMembers = new ArrayList<>();

    private final String TAG = NewGroupActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        mPresenter = new NewGroupPresenterImpl(RealmDatabase.get(this), this);
        mCurrentGroup = mPresenter.newGroup();
        setupView();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "New Group");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateUI() {
        if (mAdapter == null) {
            //mMembers = mPresenter.getUsersInGroup();
            mAdapter = new NewGroupAdapter(mMembers);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSearch(mMembers);
            mAdapter.notifyDataSetChanged();
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, NewGroupActivity.class);
        return i;
    }

    @OnClick(R.id.new_group_add_member_button)
    public void onClickGoToAddMember() {

        startActivity(AddGroupMemberActivity.newIntent(this));
    }

    @OnClick(R.id.new_group_remove_member_button)
    public void onClickRemoveSelectedFriend() {
        Log.i(TAG, mSelectedUser.getFullName() + " Removed");
    }

    @OnClick(R.id.floating_button_new_group_done)
    public void onClickFinish() {
        String groupTitle = mNameEditText.getText().toString();
        String description = mAboutEditText.getText().toString();
        mPresenter.updateGroup(mCurrentGroup, groupTitle, description, mMembers);
        finish();
    }

    @Override
    public void finished(String groupUUID) {

    }


    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int mPosition;

        @BindView(R.id.friends_row_fullname)
        TextView mFullname;
        @BindView(R.id.friends_row_username)
        TextView mUsername;

        public UserHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindUser(User user, int position) {
            mFullname.setText(user.getFullName());
            mUsername.setText(user.getUsername());
            mPosition = position;

            if (mAdapter.getSelected_position() == mPosition) {
                itemView.setBackgroundColor(ContextCompat.getColor(NewGroupActivity.this, R.color.colorHighlight));
                mSelectedUser = user;
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void onClick(View view) {
            mAdapter.notifyDataSetChanged();
            if (mPosition == mAdapter.getPrevious_position()) {
                mAdapter.setSelected_position(-1);
                mAdapter.setPrevious_position(-1);
            } else {
                mAdapter.setSelected_position(mPosition);
                mAdapter.setPrevious_position(mPosition);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public class NewGroupAdapter extends RecyclerView.Adapter<UserHolder> {

        private int selected_position = -1;
        private int previous_position = -1;
        private List<User> mUsers;

        public NewGroupAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.friends_list_row, parent, false);

            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, final int position) {
            User user = mUsers.get(position);
            holder.bindUser(user, position);

        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        public void setSearch(List<User> users) {
            mUsers = users;
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
}
