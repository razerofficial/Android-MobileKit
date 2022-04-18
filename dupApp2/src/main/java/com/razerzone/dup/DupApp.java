package com.razerzone.dup;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.razerzone.android.auth.AuthCore;
import com.razerzone.android.auth.base.BaseAuthConfig;
import com.razerzone.android.ui.UiCore;
import com.squareup.leakcanary.LeakCanary;

import static com.razerzone.dup.Flavored_constants.isStaging;


public class DupApp extends Application {

    SharedPreferences prefs;

    @Override
    public void onCreate() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate();
        BaseAuthConfig authConfig = new BaseAuthConfig.Builder().
                setIsStaging(isStaging).
                setFacebookid(getString(R.string.flavored_fb_id)).
                setGoogleid(getString(R.string.flavored_google_id)).
                setTwitchid(getString(R.string.flavored_twitch_id)).
                build();
        AuthCore.init(this, authConfig);
        UiCore.init(this, SampleProfileNavActivity.class, SampleAccountActivity.class);

    }
}
