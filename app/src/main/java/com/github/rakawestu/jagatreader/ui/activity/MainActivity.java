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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetNewsInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetNewsInteractorImpl;
import com.github.rakawestu.jagatreader.executor.MainThreadExecutor;
import com.github.rakawestu.jagatreader.executor.MainThreadExecutorImp;
import com.github.rakawestu.jagatreader.executor.ThreadExecutor;
import com.github.rakawestu.jagatreader.model.Article;
import com.github.rakawestu.jagatreader.model.news.Item;
import com.github.rakawestu.jagatreader.model.news.Rss;
import com.github.rakawestu.jagatreader.ui.adapter.ArticleAdapter;
import com.github.rakawestu.jagatreader.utils.RecyclerViewItemClickListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * @author rakawm
 */
public class MainActivity extends AppCompatActivity{

    @InjectView(R.id.progressBar)
    CircularProgressBar progressBar;
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

    private Handler handler = new Handler();
    ThreadExecutor threadExecutor;
    MainThreadExecutor mainThreadExecutor;
    private GetNewsInteractor interactor;
    private GetJagatPlayInteractor playInteractor;
    private GetJagatOcInteractor ocInteractor;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);

        //Define interactor
        threadExecutor = new ThreadExecutor();
        mainThreadExecutor = new MainThreadExecutorImp();
        interactor = new GetNewsInteractorImpl(threadExecutor, mainThreadExecutor);
        playInteractor = new GetJagatPlayInteractorImpl(threadExecutor, mainThreadExecutor);
        ocInteractor = new GetJagatOcInteractorImpl(threadExecutor, mainThreadExecutor);
        //Toolbar
        toolbar.setTitle(R.string.title_jagat_review);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(navigationView);

        setupRecyclerView();
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (!menuItem.isChecked()) {
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

    private void setupRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new ArticleAdapter(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        startActivity(intent);
                    }
                }, 300);
            }
        }));
        getFeed(1);
    }

    private void setJagatReview() {
        toolbar.setTitle(R.string.title_jagat_review);
        getFeed(1);
    }

    private void setJagatPlay() {
        toolbar.setTitle(R.string.title_jagat_play);
        getFeed(2);
    }

    private void setJagatOc() {
        toolbar.setTitle(R.string.title_jagat_oc);
        getFeed(3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFeed(int type) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        switch (type) {
            case 1:
                interactor.execute(1, new GetNewsInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        List<Article> articles = new ArrayList<Article>();
                        for (Item item: rss.getChannel().getItem()) {
                            Article article = new Article();
                            article.setTitle(item.getTitle());
                            article.setDateTime(item.getPubDate());
                            articles.add(article);
                        }
                        adapter.setArticles(articles);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Snackbar.make(mainContainer, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                playInteractor.execute(1, new GetJagatPlayInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        List<Article> articles = new ArrayList<Article>();
                        for (Item item: rss.getChannel().getItem()) {
                            Article article = new Article();
                            article.setTitle(item.getTitle());
                            article.setDateTime(item.getPubDate());
                            articles.add(article);
                        }
                        adapter.setArticles(articles);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Snackbar.make(mainContainer, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            case 3:
                ocInteractor.execute(1, new GetJagatOcInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        List<Article> articles = new ArrayList<Article>();
                        for (Item item: rss.getChannel().getItem()) {
                            Article article = new Article();
                            article.setTitle(item.getTitle());
                            article.setDateTime(item.getPubDate());
                            articles.add(article);
                        }
                        adapter.setArticles(articles);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Snackbar.make(mainContainer, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            default:
                break;
        }
    }
}
