package com.github.rakawestu.jagatreader.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.rakawestu.jagatreader.BuildConfig;
import com.github.rakawestu.jagatreader.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author rakawm
 */
public class JagatApp extends Application {
    private Tracker mTracker;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Init Joda
        JodaTimeAndroid.init(this);
        //Logging
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new Timber.DebugTree() {
            @Override
            public void e(String message, Object... args) {
                super.e(message, args);
            }

            @Override
            public void e(Throwable t, String message, Object... args) {
                super.e(t, message, args);
            }
        });
        //Set default font into Roboto-Regular
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Ubuntu-R.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        OkHttpClient okHttpClient = new OkHttpClient();
        Picasso.setSingletonInstance(new Picasso.Builder(this)
                .downloader(new OkHttpDownloader(okHttpClient))
                .build());
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker.enableExceptionReporting(true);
            mTracker.enableAdvertisingIdCollection(true);
            mTracker.enableAutoActivityTracking(true);

        }
        return mTracker;
    }
}
