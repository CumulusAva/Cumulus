package com.github.cumulusava.android.cumulus.parse;

import android.content.Context;
import android.content.Intent;

import com.github.cumulusava.android.cumulus.utils.PrefUtils;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Magnus on 2015-05-07
 * Handles receiving push-notifications
 */
public class ParseReceiver extends ParsePushBroadcastReceiver {

    @Override /** Deactivates the notification if user wants to */
    protected void onPushReceive(Context context, Intent intent) {
        if (PrefUtils.isPushEnabled(context)){
            super.onPushReceive(context, intent);
        }
    }
}