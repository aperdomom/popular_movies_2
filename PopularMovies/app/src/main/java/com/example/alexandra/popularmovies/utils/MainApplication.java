package com.example.alexandra.popularmovies.utils;

import android.app.Application;
import android.content.Context;
import roboguice.RoboGuice;

/**
 * The main application.
 */
public class MainApplication
        extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.setBaseApplicationInjector(
                this,
                RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this),
                new ApplicationModule()
        );
    }
}
