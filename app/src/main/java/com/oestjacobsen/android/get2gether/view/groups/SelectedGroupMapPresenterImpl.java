package com.oestjacobsen.android.get2gether.view.groups;

import android.content.BroadcastReceiver;

import android.location.Location;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.HashMap;


public class SelectedGroupMapPresenterImpl extends SelectedGroupParentPresenter implements SelectedGroupMapMVP.SelectedGroupMapPresenter {

    SelectedGroupMapMVP.SelectedGroupMapView mView;

    public SelectedGroupMapPresenterImpl(BaseDatabase database, String groupUIUD, SelectedGroupMapMVP.SelectedGroupMapView view) {
        super(database, groupUIUD);
        mView = view;
    }

    @Override
    public void getCurrentGroup() {
        mView.setCurrentGroup(mCurrentGroup);
    }

    @Override
    public void getCurrentUser() {
        mView.setCurrentUser(mCurrentUser);
    }

    @Override
    public void setLatLng(Location location) {
        mDatabase.updateUserPosition(mCurrentUser, location);
    }

    @Override
    public void getActiveGroups() {
        HashMap<String, Boolean> map = new HashMap<>();

        for (User member : mCurrentGroup.getParticipants()) {
            map.put(member.getUUID(), member.isGroupActive(mCurrentGroup));
        }

        mView.setActiveHashMap(map);
    }


}
