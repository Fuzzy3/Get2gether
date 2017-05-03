package com.oestjacobsen.android.get2gether.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.view.BaseActivity;
import com.oestjacobsen.android.get2gether.view.login.MainActivity;
import com.oestjacobsen.android.get2gether.view.login.PincodeActivity;
import com.oestjacobsen.android.get2gether.view.options.AboutActivity;
import com.oestjacobsen.android.get2gether.view.options.HelpActivity;


public abstract class OptionsBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void setToolbar(Toolbar toolbar, String title)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_option:
            {
                startActivity(HelpActivity.newIntent(this));
                return true;
            }
            case R.id.about_option:
            {
                startActivity(AboutActivity.newIntent(this));
                return true;
            }
            case R.id.logoff_option:
            {
                startActivity(MainActivity.newIntent(this));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
