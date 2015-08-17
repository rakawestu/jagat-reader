package com.github.rakawestu.jagatreader.app;

import android.app.Application;

import com.github.rakawestu.jagatreader.BuildConfig;
import com.github.rakawestu.jagatreader.R;
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
}
