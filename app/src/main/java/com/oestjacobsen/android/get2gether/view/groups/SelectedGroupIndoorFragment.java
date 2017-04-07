package com.oestjacobsen.android.get2gether.view.groups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.services.BeaconCollectionAdapter;
import com.oestjacobsen.android.get2gether.services.BeaconCollecterService;

import org.altbeacon.beacon.BeaconManager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectedGroupIndoorFragment extends Fragment  {

    private BeaconCollecterService mBeaconManagerService;
    private BeaconCollectionAdapter mBeaconAdapter;
    @BindView(R.id.listView) ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_indoor, container, false);
        ButterKnife.bind(this, view);

        setupView();
        return view;
    }

    private void setupView() {
        mBeaconManagerService = new BeaconCollecterService();
        mBeaconAdapter = new BeaconCollectionAdapter(getActivity());
        mListView.setAdapter(mBeaconAdapter);

    }

    public static SelectedGroupIndoorFragment newInstance() {
        SelectedGroupIndoorFragment indoorFragment = new SelectedGroupIndoorFragment();

        return indoorFragment;
    }


}
