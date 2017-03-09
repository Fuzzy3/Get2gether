package com.oestjacobsen.android.get2gether.view.groups;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oestjacobsen.android.get2gether.R;

import java.util.zip.Inflater;

import butterknife.ButterKnife;

public class SelectedGroupInfoFragment extends android.support.v4.app.Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_info, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public static SelectedGroupInfoFragment newInstance() {
        SelectedGroupInfoFragment infoFragment = new SelectedGroupInfoFragment();

        return infoFragment;
    }



}
