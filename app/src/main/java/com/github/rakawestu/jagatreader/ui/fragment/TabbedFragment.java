package com.github.rakawestu.jagatreader.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.ui.activity.NewsActivity;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenterImpl;
import com.github.rakawestu.jagatreader.utils.CustomTabLayout;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rakawm on 5/4/16.
 */
public class TabbedFragment extends Fragment{

    public static final String KEY_TYPE = "type";
    private static final int TYPE_REVIEW = 1;
    private static final int TYPE_PLAY = 2;
    private static final int TYPE_OC = 3;
    private static final String NEWS_LIST = "News List";

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tabs)
    CustomTabLayout tabLayout;
    @InjectView(R.id.pager)
    ViewPager viewPager;

    private Handler handler = new Handler();
    private Tracker mTracker;
    private PagerAdapter adapter;

    private List<String> reviewTabs, playTabs, ocTabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabbed_news, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstaceState) {
        super.onViewCreated(view, savedInstaceState);
        init();
        initTabs(getArguments().getInt(KEY_TYPE));
        //Toolbar
        switch (getArguments().getInt(KEY_TYPE)) {
            case TYPE_REVIEW:
                toolbar.setTitle(R.string.title_jagat_review);
                break;
            case TYPE_PLAY:
                toolbar.setTitle(R.string.title_jagat_play);
                break;
            case TYPE_OC:
                toolbar.setTitle(R.string.title_jagat_oc);
                break;
        }
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof NewsActivity) {
                    NewsActivity activity = (NewsActivity) getActivity();
                    activity.openDrawer();
                }
            }
        });
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
        adapter = new PagerAdapter(getChildFragmentManager(), fragments, titles);
        adapter.notifyChangeInPosition(8);
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
