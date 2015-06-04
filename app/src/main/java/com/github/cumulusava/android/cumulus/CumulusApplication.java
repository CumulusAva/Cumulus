package com.github.cumulusava.android.cumulus;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;

/**
 * Created by Magnus on 2015-05-01
 * I am not responsible for the code in this application
 * They made me write it, against my will
 *
 * Overrides Application to initialize push-notifications
 */
public class CumulusApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "",
                "");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}