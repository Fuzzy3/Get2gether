package com.oestjacobsen.android.get2gether.view.groups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oestjacobsen.android.get2gether.R;

import butterknife.ButterKnife;


public class SelectedGroupMembersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_members, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public static SelectedGroupMembersFragment newInstance() {
        SelectedGroupMembersFragment membersFragment = new SelectedGroupMembersFragment();

        return membersFragment;
    }
}
