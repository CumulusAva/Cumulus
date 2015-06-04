package com.github.cumulusava.android.cumulus.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.github.cumulusava.android.cumulus.R;

/**
 * Created by Magnus on 2015-05-07
 * Utility class to get settings easy
 */
public class PrefUtils {
    private static final int KEY_PUSH = R.string.pref_push_key;
    private static final int KEY_TOOLBAR = R.string.pref_toolbar_key;

    public static boolean isPushEnabled(final Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(KEY_PUSH), true);
    }

    public static boolean shouldHideToolbar(final Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(KEY_TOOLBAR), true);
    }
}