package com.github.rakawestu.jagatreader.app;

import android.app.Application;

import com.github.rakawestu.jagatreader.BuildConfig;

import timber.log.Timber;

/**
 * @author rakawm
 */
public class JagatApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Logging
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new Timber.DebugTree() {
            @Override
            public void e(String message, Object... args) {
                super.e(message, args);
            }

            @Override
            public void e(Throwable t, String message, Object... args) {
                super.e(t, message, args);}
        });
    }
}
