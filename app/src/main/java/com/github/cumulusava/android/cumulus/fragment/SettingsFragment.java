package com.github.cumulusava.android.cumulus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.cumulusava.android.cumulus.R;

/**
 * Created by Magnus on 2015-05-07
 * Fragment for showing settings
 */
public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        findPreference(getString(R.string.pref_toolbar_key)).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        showRestartSnackBar();
        return true;
    }

    private void showRestartSnackBar(){
        Snackbar.make(getView(), R.string.pref_changed, Snackbar.LENGTH_SHORT)
                .setAction(R.string.pref_snackBar_restart, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent restart = getActivity().getPackageManager()
                                .getLaunchIntentForPackage(getActivity().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.blue_primary))
                .show();
    }
}