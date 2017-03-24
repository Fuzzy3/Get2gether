package com.oestjacobsen.android.get2gether.view.groups;


import android.widget.Toast;

import com.oestjacobsen.android.get2gether.model.Group;

public abstract class SelectedGroupParentView extends android.support.v4.app.Fragment implements SelectedGroupMVP.SelectedGroupView {

    protected Group mCurrentGroup;

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCurrentGroup(Group group) {
        mCurrentGroup = group;
    }
}
