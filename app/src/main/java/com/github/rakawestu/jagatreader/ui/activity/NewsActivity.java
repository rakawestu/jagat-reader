package com.github.rakawestu.jagatreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.view.MenuItem;
import android.view.View;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.model.Article;
import com.github.rakawestu.jagatreader.ui.adapter.ArticleAdapter;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenter;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenterImpl;
import com.github.rakawestu.jagatreader.ui.view.NewsView;
import com.github.rakawestu.jagatreader.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author rakawm
 */
public class NewsActivity extends AppCompatActivity implements NewsView {

    private static final int TYPE_REVIEW = 1;
    private static final int TYPE_PLAY = 2;
    private static final int TYPE_OC = 3;

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

    private Handler handler = new Handler();
    private NewsPresenter presenter = new NewsPresenterImpl();
    private ArticleAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);
        setup();
        presenter.setView(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.onViewCreate();
            }
        }, 300);
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
                if (!menuItem.isChecked()) {
                    adapter.setArticles(new ArrayList<Article>());
                    adapter.notifyDataSetChanged();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setJagatReview();
                                }
                            }, 300);
                            break;
                        case R.id.nav_play:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setJagatPlay();
                                }
                            }, 300);
                            break;
                        case R.id.nav_oc:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setJagatOc();
                                }
                            }, 300);
                            break;
                    }
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return false;
            }
        });
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
        recyclerView.setAdapter(adapter);
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
    }


    @Override
    public void onError(Throwable throwable) {
        Snackbar.make(mainContainer, throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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
        }
        return super.onOptionsItemSelected(item);
    }
}
