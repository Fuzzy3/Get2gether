package com.oestjacobsen.android.get2gether.view.groups;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectedGroupInfoFragment extends SelectedGroupParentView implements SelectedGroupInfoMVP.SelectedGroupInfoView {

    @BindView(R.id.selected_group_description) TextView mDescription;

    private static String ARGS_GROUP_UUID = "ARGSGROUPUUID";
    private SelectedGroupInfoMVP.SelectedGroupInfoPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_info, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new SelectedGroupInfoPresenterImpl(RealmDatabase.get(getContext()), this, getArguments().getString(ARGS_GROUP_UUID));
        mPresenter.getCurrentGroup();
        return view;
    }

    @Override
    public void setCurrentGroup(Group group) {
        super.setCurrentGroup(group);
        setupView();
    }

    private void setupView() {
        mDescription.setText(mCurrentGroup.getGroupDesc());
    }

    @OnClick(R.id.selected_group_edit_button)
    public void onClickEditGroupButton() {
        startActivity(NewGroupActivity.newIntentWithGroup(getContext(), mCurrentGroup.getUUID()));
    }

    @OnClick(R.id.selected_group_leave_group_button)
    public void onClickLeaveGroupButton() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        //YES CLICKED
                        mPresenter.removeUserFromGroup();
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        //NO CLICKED
                        return;
                    }
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to leave the group " + mCurrentGroup.getGroupTitle() + "?")
                .setPositiveButton("yes", dialogClickListener)
                .setNegativeButton("no", dialogClickListener)
                .show();
    }

    public static SelectedGroupInfoFragment newInstance(String groupUUID) {
        SelectedGroupInfoFragment infoFragment = new SelectedGroupInfoFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_GROUP_UUID, groupUUID);
        infoFragment.setArguments(args);

        return infoFragment;
    }

    @Override
    public void groupRemoved() {

        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
