package com.oestjacobsen.android.get2gether.view.friends;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.OptionsBaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendActivity extends OptionsBaseActivity implements AddFriendMVP.AddFriendView {

    @BindView(R.id.friends_toolbar) Toolbar mToolbar;
    @BindView(R.id.add_friend_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.search_friend_edittext) EditText mSearchInput;

    private AddFriendMVP.AddFriendPresenter mPresenter;
    private AddFriendAdapter mAdapter;
    private List<User> mSearchResult;
    private User mSelectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }

    @Override
    protected void setupViewFirst() {
        super.setupViewFirst();
        mPresenter = new AddFriendPresenterImpl(RealmDatabase.get(this), this);
        setupView();
        updateUI();
    }

    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "Add Friend");
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateUI() {
        if(mAdapter == null) {
            mSearchResult = mPresenter.getAllUsers();
            mAdapter = new AddFriendAdapter(mSearchResult);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSearch(mSearchResult);
            mAdapter.notifyDataSetChanged();
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, AddFriendActivity.class);
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

    @OnClick(R.id.floating_button_add_selected_friend)
    public void onClickAddSelectedFriend() {
        if(mSelectedUser != null) {
            mPresenter.addFriendInvite(mSelectedUser);
        } else {
            Toast.makeText(this, "No friend selected", Toast.LENGTH_SHORT).show();
        }
    }



    @OnClick(R.id.search_friend_button)
    public void onClickSearchFriend() {
        String input = mSearchInput.getText().toString();
        mSearchResult = mPresenter.getUsersMatchingString(input);
        updateUI();
    }

    @Override
    public void finished() {
        finish();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int mPosition;

        @BindView(R.id.friends_row_fullname) TextView mFullname;
        @BindView(R.id.friends_row_uuid) TextView mUUID;

        public UserHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindUser(User user, int position) {
            mFullname.setText(user.getFullName());
            mUUID.setText(user.getUUID());
            mPosition = position;

            if(mAdapter.getSelected_position() == mPosition) {
                itemView.setBackgroundColor(ContextCompat.getColor(AddFriendActivity.this, R.color.colorHighlight));
                mSelectedUser = user;
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void onClick(View view) {
            mAdapter.notifyDataSetChanged();
            if(mPosition == mAdapter.getPrevious_position()) {
                mAdapter.setSelected_position(-1);
                mAdapter.setPrevious_position(-1);
                mSelectedUser = null;
            } else {
                mAdapter.setSelected_position(mPosition);
                mAdapter.setPrevious_position(mPosition);

            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public class AddFriendAdapter extends RecyclerView.Adapter<UserHolder>  {

        private int selected_position = -1;
        private int previous_position = -1;
        private List<User> mUsers;

        public AddFriendAdapter(List<User> users) {
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
