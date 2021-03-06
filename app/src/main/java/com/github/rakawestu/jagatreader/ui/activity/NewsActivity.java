package com.github.rakawestu.jagatreader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.rakawestu.jagatreader.BuildConfig;
import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.app.Constant;
import com.github.rakawestu.jagatreader.app.JagatApp;
import com.github.rakawestu.jagatreader.model.Article;
import com.github.rakawestu.jagatreader.ui.adapter.ArticleAdapter;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenter;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenterImpl;
import com.github.rakawestu.jagatreader.ui.view.NewsView;
import com.github.rakawestu.jagatreader.utils.RecyclerViewItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appinvite.AppInviteInvitation;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author rakawm
 */
public class NewsActivity extends AppCompatActivity implements NewsView {

    private static final int REQUEST_INVITE = 1002;
    private static final int TYPE_REVIEW = 1;
    private static final int TYPE_PLAY = 2;
    private static final int TYPE_OC = 3;
    private static final String NEWS_LIST = "News List";
    private static final String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";

    @InjectView(R.id.containerMain)
    CoordinatorLayout mainContainer;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigationView)
    NavigationView navigationView;
    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsPresenter presenter = new NewsPresenterImpl();
    private ArticleAdapter adapter;
    private SlideInBottomAnimationAdapter animationAdapter;
    private LinearLayoutManager layoutManager;
    private boolean loading;
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
        presenter.setView(this);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(Constant.CATEGORY_NEWS)
                .setAction(Constant.ACTION_GET)
                .setLabel(Constant.GET_JAGAT_REVIEW)
                .build());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.onViewCreate();
            }
        }, 300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
        //Recycler View
        layoutManager = new LinearLayoutManager(this);
        resetAdapter();
        setupRecyclerView();
        //Swipe Refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });
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
        presenter.changeType(TYPE_REVIEW);
    }

    private void setJagatPlay() {
        toolbar.setTitle(R.string.title_jagat_play);
        presenter.changeType(TYPE_PLAY);
    }

    private void setJagatOc() {
        toolbar.setTitle(R.string.title_jagat_oc);
        presenter.changeType(TYPE_OC);
    }

    private void resetAdapter() {
        adapter = new ArticleAdapter(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.onNews(adapter.getItemData(position));
                    }
                }, 300);
            }
        }));
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 1) {
                    if (lastVisibleItem >= totalItemCount - 1) {
                        if (!loading) {
                            presenter.loadMore();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void showDetails(Article article) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    @Override
    public void showContent() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
        loading = true;
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    @Override
    public void addData(List<Article> data) {
        adapter.setArticles(data);
        adapter.notifyDataSetChanged();
        animationAdapter.notifyDataSetChanged();
    }


    @Override
    public void onError(Throwable throwable) {
        Snackbar.make(mainContainer, "Gagal mengambil berita.", Snackbar.LENGTH_INDEFINITE).setAction(R.string.action_retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() > 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onRefresh();
                        }
                    }, 300);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onViewCreate();
                        }
                    }, 300);
                }
            }
        }).show();
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
                invitePeoples();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void invitePeoples() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
}
