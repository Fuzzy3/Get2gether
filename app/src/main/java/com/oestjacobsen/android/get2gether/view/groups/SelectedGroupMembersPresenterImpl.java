package com.oestjacobsen.android.get2gether.view.groups;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.HashMap;
import java.util.List;


public class SelectedGroupMembersPresenterImpl extends SelectedGroupParentPresenter implements SelectedGroupMembersMVP.SelectedGroupMembersPresenter {

    SelectedGroupMembersMVP.SelectedGroupMembersView mView;

    public SelectedGroupMembersPresenterImpl(BaseDatabase database, String groupUIUD, SelectedGroupMembersMVP.SelectedGroupMembersView view) {
        super(database, groupUIUD);
        mView = view;
    }

    @Override
    public void getCurrentGroup() {
        mView.setCurrentGroup(mCurrentGroup);
    }

    @Override
    public void getActiveGroups() {
        HashMap<String, Boolean> map = new HashMap<>();
        int activeCount = 0;
        int inActiveCount = 0;

        for(User member : mCurrentGroup.getParticipants()) {
            boolean isActive = member.isGroupActive(mCurrentGroup);
            map.put(member.getUUID(), isActive);
            if(isActive) {
                activeCount++;
            } else {
                inActiveCount++;
            }
        }

        mView.setNumberOfActive(activeCount);
        mView.setNumberOfInActive(inActiveCount);
        mView.setActiveHashMap(map);
    }


}
