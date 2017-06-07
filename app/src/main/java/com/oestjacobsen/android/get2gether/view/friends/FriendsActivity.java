package com.oestjacobsen.android.get2gether.view.friends;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.MainMenuActivity;
import com.oestjacobsen.android.get2gether.view.OptionsBaseActivity;
import com.oestjacobsen.android.get2gether.view.groups.GroupsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsActivity extends OptionsBaseActivity implements FriendsMVP.FriendsView{

    @BindView(R.id.friends_toolbar) Toolbar mToolbar;
    @BindView(R.id.friends_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.add_pending_friend_button) Button mAddPendingFriendButton;
    @BindView(R.id.friends_swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    private List<User> mFriendsAndPending = new ArrayList<>();
    private FriendsMVP.FriendsPresenter mPresenter;
    private FriendsAdapter mAdapter;
    private User mSelectedUser;
    private int mPendingStarting = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
    }

    @Override
    protected void setupViewFirst() {
        super.setupViewFirst();
        mPresenter = new FriendsPresenterImpl(RealmDatabase.get(this), this, getApplicationContext());
        setupView();
    }


    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "Friends");
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAddPendingFriendButton.setVisibility(View.INVISIBLE);
        mPresenter.getFriendsAndPending();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getFriendsAndPending();
            }
        });
    }


    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, FriendsActivity.class);
        return i;
    }


    private void updateUI() {
        if(mAdapter == null) {
            mAdapter = new FriendsAdapter(mFriendsAndPending);
            mAdapter.setPendingStartingPos(mPendingStarting);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setFriendsList(mFriendsAndPending);
            mAdapter.setPendingStartingPos(mPendingStarting);
            mAdapter.notifyDataSetChanged();
        }

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


    @OnClick(R.id.add_friend_button)
    public void onClickAddFriend() {
        startActivity(AddFriendActivity.newIntent(this));
    }

    @OnClick(R.id.add_pending_friend_button)
    public void onClickAddPending() {
        mPresenter.addPendingFriend(mSelectedUser);
    }


    @Override
    public void showFriendsAndPending(List<User> friendsandpending, int pendingStartingPosition) {
        mFriendsAndPending = friendsandpending;
        mPendingStarting = pendingStartingPosition;
        mSelectedUser = null;
        mAddPendingFriendButton.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        updateUI();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //Adapter and viewholder for recyclerview
    public class FriendHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.friends_row_fullname) TextView mFullname;
        @BindView(R.id.friends_row_uuid) TextView mUUID;
        private boolean mIsPending;
        private int mPosition;


        public FriendHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindFriend(User friend, int position, boolean isPending) {
            mIsPending = isPending;
            mPosition = position;
            mFullname.setText(friend.getFullName());
            mUUID.setText(friend.getUUID());
            if(mIsPending) {
                if (mAdapter.getSelected_position() == mPosition) {
                    itemView.setBackgroundColor(ContextCompat.getColor(FriendsActivity.this, R.color.colorHighlight));
                    mSelectedUser = friend;
                } else {
                    itemView.setBackgroundColor(ContextCompat.getColor(FriendsActivity.this, R.color.colorPending));
                }
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void onClick(View view) {
            if(mIsPending){
                mAdapter.notifyDataSetChanged();
                if (mPosition == mAdapter.getPrevious_position()) {
                    mAdapter.setSelected_position(-1);
                    mAdapter.setPrevious_position(-1);
                    mAddPendingFriendButton.setVisibility(View.INVISIBLE);

                } else {
                    mAdapter.setSelected_position(mPosition);
                    mAdapter.setPrevious_position(mPosition);
                    mAddPendingFriendButton.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public class FriendsAdapter extends RecyclerView.Adapter<FriendHolder> {

        private List<User> mFriends;
        private int mPendingStartingPos = 0;
        private int selected_position = -1;
        private int previous_position = -1;


        public FriendsAdapter(List<User> friends) {
            mFriends = friends;
        }

        @Override
        public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.friends_list_row, parent, false);

            return new FriendHolder(view);
        }

        @Override
        public void onBindViewHolder(FriendHolder holder, int position) {
            User friend = mFriends.get(position);
            if(position >= mPendingStartingPos) {
                holder.bindFriend(friend, position, true);
            } else {
                holder.bindFriend(friend, position, false);
            }
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }

        public void setFriendsList(List<User> friends) {
            mFriends = friends;
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

}
