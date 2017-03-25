package com.oestjacobsen.android.get2gether.view.groups;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.vision.text.Text;
import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectedGroupMembersFragment extends SelectedGroupParentView implements SelectedGroupMembersMVP.SelectedGroupMembersView {

    @BindView(R.id.active_members_number) TextView mActiveTextView;
    @BindView(R.id.inactive_members_number) TextView mInActiveTextView;
    @BindView(R.id.selected_group_members_recyclerview) RecyclerView mRecyclerView;

    private static final String ARGS_GROUP_UUID = "ARGSGROUPUUID";
    private SelectedGroupMembersMVP.SelectedGroupMembersPresenter mPresenter;
    private List<User> mMembersList;
    private MembersAdapter mAdapter;
    private HashMap<String, Boolean> mActiveMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_members, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new SelectedGroupMembersPresenterImpl(RealmDatabase.get(getContext()),getArguments().getString(ARGS_GROUP_UUID), this);
        setupView();
        return view;
    }

    public static SelectedGroupMembersFragment newInstance(String groupUUID) {
        SelectedGroupMembersFragment membersFragment = new SelectedGroupMembersFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_GROUP_UUID, groupUUID);
        membersFragment.setArguments(args);

        return membersFragment;
    }

    private void setupView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPresenter.getCurrentGroup();
        mPresenter.getActiveGroups();
    }

    @Override
    public void setCurrentGroup(Group group) {
        super.setCurrentGroup(group);
        mMembersList = mCurrentGroup.getParticipants();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new MembersAdapter(mMembersList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setList(mMembersList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setActiveHashMap(HashMap<String, Boolean> map) {
        mActiveMap = map;
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setNumberOfActive(int num) {
        mActiveTextView.setText(num+"");

    }

    @Override
    public void setNumberOfInActive(int num) {
        mInActiveTextView.setText(num+"");
    }


    public class UserHolder extends RecyclerView.ViewHolder {
        private int mPosition;

        @BindView(R.id.friends_row_fullname)
        TextView mFullname;
        @BindView(R.id.friends_row_username)
        TextView mUsername;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindUser(User user, int position) {
            mFullname.setText(user.getFullName());
            mUsername.setText(user.getUsername());
            if (mActiveMap.get(user.getUUID())) {
                itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGroupActive));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGroupInActive));
            }
        }

    }

    public class MembersAdapter extends RecyclerView.Adapter<UserHolder> {

        private List<User> mUsers;

        public MembersAdapter(List<User> users) {
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

        public void setList(List<User> list) {
            mUsers = list;
        }
    }
}
