package com.github.cumulusava.android.cumulus.activity;

import android.os.Bundle;

import com.github.cumulusava.android.cumulus.R;
import com.github.cumulusava.android.cumulus.fragment.SettingsFragment;

/**
 * Created by Elev on 2015-06-03
 */
public class SettingsActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle(R.string.action_read_list);

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, SettingsFragment.newInstance())
                .commit();
    }
}