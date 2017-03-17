package com.oestjacobsen.android.get2gether.view.profile;

import com.oestjacobsen.android.get2gether.model.BaseDatabase;

/**
 * Created by Oest Balmer on 17-03-2017.
 */

public class EditProfilePresenterImpl implements EditProfileMVP.EditProfilePresenter {

    private EditProfileMVP.EditProfileView mView;
    private BaseDatabase mDatabase;

    public EditProfilePresenterImpl(BaseDatabase database, EditProfileMVP.EditProfileView view) {
        mDatabase = database;
        mView = view;
    }



}
