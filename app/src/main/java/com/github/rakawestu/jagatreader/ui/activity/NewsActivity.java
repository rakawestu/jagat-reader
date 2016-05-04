package com.github.rakawestu.jagatreader.ui.activity;

import com.github.rakawestu.jagatreader.ui.fragment.TabbedFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.rakawestu.jagatreader.BuildConfig;
import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.app.Constant;
import com.github.rakawestu.jagatreader.app.JagatApp;
import com.github.rakawestu.jagatreader.ui.fragment.NewsListFragment;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenterImpl;
import com.github.rakawestu.jagatreader.utils.CustomTabLayout;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * @author rakawm
 */
public class NewsActivity extends AppCompatActivity {

    private static final int REQUEST_INVITE = 1002;
    private static final int TYPE_REVIEW = 1;
    private static final int TYPE_PLAY = 2;
    private static final int TYPE_OC = 3;
    private static final String NEWS_LIST = "News List";

    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigationView)
    NavigationView navigationView;

    private Handler handler = new Handler();
    private Tracker mTracker;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);
        // Obtain the shared Tracker instance.
        JagatApp application = (JagatApp) getApplication();
        mTracker = application.getDefaultTracker();
        setup();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(Constant.CATEGORY_NEWS)
                .setAction(Constant.ACTION_GET)
                .setLabel(Constant.GET_JAGAT_REVIEW)
                .build());
    }

    private void initTabs(int type) {
        TabbedFragment fragment = new TabbedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TabbedFragment.KEY_TYPE, type);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).disallowAddToBackStack().commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("Setting screen name: " + NEWS_LIST);
        mTracker.setScreenName(NEWS_LIST);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setup() {
        //Drawer
        setupDrawerContent();

        initTabs(TYPE_REVIEW);
    }

    private void setupDrawerContent() {
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory(Constant.CATEGORY_NEWS)
                                .setAction(Constant.ACTION_GET)
                                .setLabel(Constant.GET_JAGAT_REVIEW)
                                .build());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setJagatReview();
                            }
                        }, 300);
                        break;
                    case R.id.nav_play:
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory(Constant.CATEGORY_NEWS)
                                .setAction(Constant.ACTION_GET)
                                .setLabel(Constant.GET_JAGAT_PLAY)
                                .build());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setJagatPlay();
                            }
                        }, 300);
                        break;
                    case R.id.nav_oc:
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory(Constant.CATEGORY_NEWS)
                                .setAction(Constant.ACTION_GET)
                                .setLabel(Constant.GET_JAGAT_OC)
                                .build());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setJagatOc();
                            }
                        }, 300);
                        break;
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView versionName = (TextView) findViewById(R.id.version);
                versionName.setText("Versi " + BuildConfig.VERSION_NAME);
            }
        }, 300);
    }

    private void setJagatReview() {
        initTabs(TYPE_REVIEW);
    }

    private void setJagatPlay() {
        initTabs(TYPE_PLAY);
    }

    private void setJagatOc() {
        initTabs(TYPE_OC);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
