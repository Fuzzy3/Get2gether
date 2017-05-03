package com.oestjacobsen.android.get2gether.view.groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import com.oestjacobsen.android.get2gether.view.OptionsBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGroupActivity extends OptionsBaseActivity implements NewGroupMVP.NewGroupView{

    @BindView(R.id.new_group_name_edit_text)
    EditText mNameEditText;
    @BindView(R.id.new_group_about_edit_text)
    EditText mAboutEditText;
    @BindView(R.id.new_group_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.new_group_show_members_recyclerview) RecyclerView mRecyclerView;

    private NewGroupMVP.NewGroupPresenter mPresenter;
    private User mSelectedUser;
    private Group mCurrentGroup;
    private NewGroupAdapter mAdapter;
    private ArrayList<User> mMembers = new ArrayList<>();

    private static final String GROUPUUIDARGS = "GROUPUUIDARGS";
    private final String TAG = NewGroupActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        mPresenter = new NewGroupPresenterImpl(RealmDatabase.get(this), this);
        if(getIntent().getStringExtra(GROUPUUIDARGS) != null) {
            mPresenter.editExistingGroup(getIntent().getStringExtra(GROUPUUIDARGS));
        } else {
            mPresenter.editNewGroup();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setGroup(Group group) {
        mCurrentGroup = group;
        setupView();
        updateUI();
    }

    private void setupView() {
        ButterKnife.bind(this);

        setToolbar(mToolbar, "New Group");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNameEditText.setText(mCurrentGroup.getGroupTitle());
        mAboutEditText.setText(mCurrentGroup.getGroupDesc());
        mMembers = mCurrentGroup.getParticipantsInArrayList();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new NewGroupAdapter(mMembers);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSearch(mMembers);
            mAdapter.notifyDataSetChanged();
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, NewGroupActivity.class);
        return i;
    }

    public static Intent newIntentWithGroup(Context packageContext, String groupUUID) {
        Intent i = new Intent(packageContext, NewGroupActivity.class);
        i.putExtra(GROUPUUIDARGS, groupUUID);
        return i;
    }

    @OnClick(R.id.new_group_add_member_button)
    public void onClickGoToAddMember() {
        startActivityForResult(AddGroupMemberActivity.newIntent(this), 1);
    }

    @OnClick(R.id.new_group_remove_member_button)
    public void onClickRemoveSelectedFriend() {
        if(mSelectedUser != null) {
            mPresenter.removeMemberFromList(mMembers, mSelectedUser);
        } else {
            Toast.makeText(this, "Select a user to delete", Toast.LENGTH_SHORT).show();
        }
    }



    @OnClick(R.id.floating_button_new_group_done)
    public void onClickFinish() {
        if(!mNameEditText.getText().toString().equals("")) {
            String groupTitle = mNameEditText.getText().toString();
            String description = mAboutEditText.getText().toString();
            mPresenter.updateGroup(mCurrentGroup, groupTitle, description, mMembers);
        } else {
            Toast.makeText(this, "Group needs a name", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void finished() {
        finish();
    }

    @Override
    public void memberSuccesfullyRemoved(ArrayList<User> newlist) {
        mMembers = newlist;
        mSelectedUser = null;
        mAdapter.setSelected_position(-1);
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            Bundle extra = data.getExtras();
            String UserUUID = extra.getString("UUID").trim();
            mMembers.add(mPresenter.getUserFromUUID(UserUUID));
            updateUI();
        }
    }


    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int mPosition;

        @BindView(R.id.friends_row_fullname)
        TextView mFullname;
        @BindView(R.id.friends_row_uuid)
        TextView mUUID;

        public UserHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindUser(User user, int position) {
            mFullname.setText(user.getFullName());
            mUUID.setText(user.getUUID());
            mPosition = position;

            if (mAdapter.getSelected_position() == mPosition) {
                itemView.setBackgroundColor(ContextCompat.getColor(NewGroupActivity.this, R.color.colorHighlight));
                mSelectedUser = user;
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void onClick(View view) {
            mAdapter.notifyDataSetChanged();
            if (mPosition == mAdapter.getPrevious_position()) {
                mAdapter.setSelected_position(-1);
                mAdapter.setPrevious_position(-1);
            } else {
                mAdapter.setSelected_position(mPosition);
                mAdapter.setPrevious_position(mPosition);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public class NewGroupAdapter extends RecyclerView.Adapter<UserHolder> {

        private int selected_position = -1;
        private int previous_position = -1;
        private List<User> mUsers;

        public NewGroupAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.friends_list_row, parent, false);

            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, final int position) {
            User user = mUsers.get(position);
            holder.bindUser(user, position);

        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        public void setSearch(List<User> users) {
            mUsers = users;
        }

        public int getSelected_position() {
            return selected_position;
        }

        public void setSelected_position(int selected_position) {
            this.selected_position = selected_position;
        }

        public int getPrevious_position() {
            return previous_position;
        }

        public void setPrevious_position(int previous_position) {
            this.previous_position = previous_position;
        }

    }
}
