package com.oestjacobsen.android.get2gether.view.groups;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;

import java.util.zip.Inflater;

import butterknife.ButterKnife;

public class SelectedGroupInfoFragment extends SelectedGroupParentView implements SelectedGroupInfoMVP.SelectedGroupInfoView {

    private static String ARGS_GROUP_UUID = "ARGSGROUPUUID";
    private String groupUUID;
    private SelectedGroupInfoMVP.SelectedGroupInfoPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_info, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new SelectedGroupInfoPresenterImpl(RealmDatabase.get(getContext()), this, getArguments().getString(ARGS_GROUP_UUID));

        return view;
    }

    public static SelectedGroupInfoFragment newInstance(String groupUUID) {
        SelectedGroupInfoFragment infoFragment = new SelectedGroupInfoFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_GROUP_UUID, groupUUID);
        infoFragment.setArguments(args);

        return infoFragment;
    }





}
