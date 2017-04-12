package com.oestjacobsen.android.get2gether.view.groups;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.services.BeaconCollecterService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectedGroupIndoorFragment extends Fragment implements SelectedGroupIndoorMVP.SelectedGroupIndoorView {

    private LocationsAdapter mAdapter;
    private SelectedGroupIndoorMVP.SelectedGroupIndoorPresenter mPresenter;
    private Group mCurrentGroup;
    private List<User> mMembers;
    @BindView(R.id.selected_group_indoor_recyclerview) RecyclerView mRecyclerView;
    private static final String ARGS_GROUP_UUID = "ARGSGROUPUUID";
    private BroadcastReceiver mIndoorReciever;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_indoor, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new SelectedGroupIndoorPresenterImpl(RealmDatabase.get(getContext()), this, getArguments().getString(ARGS_GROUP_UUID));
        setupView();
        return view;
    }

    private void setupView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPresenter.getCurrentGroup();
    }

    private void updateUI() {
        if(mAdapter == null) {
            mAdapter = new LocationsAdapter(mMembers);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setList(mMembers);
            mAdapter.notifyDataSetChanged();
        }
    }

    public static SelectedGroupIndoorFragment newInstance(String groupUUID) {
        SelectedGroupIndoorFragment indoorFragment = new SelectedGroupIndoorFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_GROUP_UUID, groupUUID);
        indoorFragment.setArguments(args);

        return indoorFragment;
    }


    @Override
    public void setCurrentGroup(Group group) {
        mCurrentGroup = group;
        mMembers = mCurrentGroup.getParticipants();
        createLocationBroadcastReciever();
        updateUI();
    }

    private void createLocationBroadcastReciever() {
        mIndoorReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                newIndoorLocationsAcquired();
            }
        };
    }

    private void newIndoorLocationsAcquired() {
        Log.i(getTag(), "Indoor broadcast recived, time to get some updates");
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mIndoorReciever);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mIndoorReciever, new IntentFilter(BeaconCollecterService.BROADCAST_INDOOR_ACTION));

    }


    public class LocationHolder extends RecyclerView.ViewHolder {
        private int mPosition;

        @BindView(R.id.beacon_item_user_fullname)
        TextView mFullname;
        @BindView(R.id.beacon_item_user_location)
        TextView mLocation;

        public LocationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindUser(User user, int position) {
            mFullname.setText(user.getFullName());
            mLocation.setText(user.getIndoorLocation());
        }

    }

    public class LocationsAdapter extends RecyclerView.Adapter<LocationHolder> {

        private List<User> mUsers;

        public LocationsAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.beacon_list_item, parent, false);

            return new LocationHolder(view);
        }

        @Override
        public void onBindViewHolder(LocationHolder holder, final int position) {
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
