package com.oestjacobsen.android.get2gether.view.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oestjacobsen.android.get2gether.InMemorySession;
import com.oestjacobsen.android.get2gether.CurrentUser;
import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.UserBaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsActivity extends UserBaseActivity implements FriendsMVP.FriendsView{

    @BindView(R.id.friends_toolbar) Toolbar mToolbar;
    @BindView(R.id.friends_recycler_view) RecyclerView mRecyclerView;

    private FriendsMVP.FriendsPresenter mPresenter;
    private FriendsAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        mPresenter = new FriendsPresenterImpl(RealmDatabase.get(this), this);

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


    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, FriendsActivity.class);
        return i;
    }


    private void updateUI() {
        mAdapter = new FriendsAdapter(mPresenter.getFriends());
        mRecyclerView.setAdapter(mAdapter);
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


    @OnClick(R.id.floating_button_add_friend)
    public void onClickAddFriend() {
        startActivity(AddFriendActivity.newIntent(this));
    }



    //Adapter and viewholder for recyclerview
    public class FriendHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.friends_row_fullname) TextView mFullname;
        @BindView(R.id.friends_row_username) TextView mUsername;

        public FriendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindFriend(User friend) {
            mFullname.setText(friend.getFullName());
            mUsername.setText(friend.getUsername());
        }
    }

    public class FriendsAdapter extends RecyclerView.Adapter<FriendHolder> {

        private List<User> mFriends;

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
            holder.bindFriend(friend);
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }

}
