package com.github.rakawestu.jagatreader.ui.activity;

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

    @InjectView(R.id.containerMain)
    CoordinatorLayout mainContainer;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigationView)
    NavigationView navigationView;
    @InjectView(R.id.tabs)
    CustomTabLayout tabLayout;
    @InjectView(R.id.pager)
    ViewPager viewPager;

    private Handler handler = new Handler();
    private Tracker mTracker;
    private PagerAdapter adapter;

    private List<String> reviewTabs, playTabs, ocTabs;

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
        tabLayout.removeAllTabs();
        switch (type) {
            case TYPE_REVIEW:
                for (String tabName: reviewTabs) {
                    tabLayout.addTab(tabLayout.newTab().setText(tabName));
                }
                break;
            case TYPE_PLAY:
                for (String tabName: playTabs) {
                    tabLayout.addTab(tabLayout.newTab().setText(tabName));
                }
                break;
            case TYPE_OC:
                for (String tabName: ocTabs) {
                    tabLayout.addTab(tabLayout.newTab().setText(tabName));
                }
                break;
        }
        initViewPager(type);
    }

    private void initViewPager(int type) {
        List<Fragment> fragments = null;
        List<String> titles = null;
        switch (type) {
            case TYPE_REVIEW:
                fragments = getJagatReviewFragments();
                titles = reviewTabs;
                break;
            case TYPE_PLAY:
                fragments = getJagatPlayFragments();
                titles = playTabs;
                break;
            case TYPE_OC:
                fragments = getJagatOcFragments();
                titles = ocTabs;
                break;
        }
        adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);
        adapter.notifyChangeInPosition(6);
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void init() {
        // Init review tabs
        reviewTabs = new ArrayList<>();
        reviewTabs.add("Latest");
        reviewTabs.add("Review");
        reviewTabs.add("Gadget");
        reviewTabs.add("Release");

        // Init play tabs
        playTabs = new ArrayList<>();
        playTabs.add("Features");
        playTabs.add("PC");
        playTabs.add("Playstation");
        playTabs.add("Nintendo");
        playTabs.add("Gear");
        playTabs.add("Top");

        // Init oc tabs
        ocTabs = new ArrayList<>();
        ocTabs.add("Latest");
        ocTabs.add("Review");
        ocTabs.add("Competition");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("Setting screen name: " + NEWS_LIST);
        mTracker.setScreenName(NEWS_LIST);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setup() {
        //Toolbar
        toolbar.setTitle(R.string.title_jagat_review);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Drawer
        setupDrawerContent();

        init();
        initTabs(1);
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
        toolbar.setTitle(R.string.title_jagat_review);
        initTabs(TYPE_REVIEW);
    }

    private void setJagatPlay() {
        toolbar.setTitle(R.string.title_jagat_play);
        initTabs(TYPE_PLAY);
    }

    private void setJagatOc() {
        toolbar.setTitle(R.string.title_jagat_oc);
        initTabs(TYPE_OC);
    }

    private List<Fragment> getJagatReviewFragments() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i=0;i<4;i++) {
            NewsListFragment fragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_REVIEW_LATEST);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_REVIEW_REVIEW);
                    fragment.setArguments(bundle);
                    break;
                case 2:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_REVIEW_GADGET);
                    fragment.setArguments(bundle);
                    break;
                case 3:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_REVIEW_RELEASE);
                    fragment.setArguments(bundle);
                    break;
            }
            fragments.add(fragment);
        }
        return fragments;
    }

    private List<Fragment> getJagatPlayFragments() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i=0;i<6;i++) {
            NewsListFragment fragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_PLAY_LATEST);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_PLAY_PC);
                    fragment.setArguments(bundle);
                    break;
                case 2:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_PLAY_PS);
                    fragment.setArguments(bundle);
                    break;
                case 3:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_PLAY_NINTENDO);
                    fragment.setArguments(bundle);
                    break;
                case 4:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_PLAY_GEAR);
                    fragment.setArguments(bundle);
                    break;
                case 5:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_PLAY_TOP);
                    fragment.setArguments(bundle);
                    break;
            }
            fragments.add(fragment);
        }
        return fragments;
    }

    private List<Fragment> getJagatOcFragments() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i=0;i<3;i++) {
            NewsListFragment fragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_OC_LATEST);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_OC_REVIEW);
                    fragment.setArguments(bundle);
                    break;
                case 2:
                    bundle.putInt(NewsListFragment.ARG_TYPE, NewsPresenterImpl.TYPE_OC_COMPETITION);
                    fragment.setArguments(bundle);
                    break;
            }
            fragments.add(fragment);
        }
        return fragments;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_invite:
                //invitePeoples();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;
        List<String> titles;
        private long baseId = 0;

        public PagerAdapter(FragmentManager fm,
                            List<Fragment> fragments,
                            List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        //this is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         *
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }
    }
}
