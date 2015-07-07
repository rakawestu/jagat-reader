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
import android.widget.RelativeLayout;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import timber.log.Timber;

/**
 * @author rakawm
 */
public class MainActivity extends AppCompatActivity{

    @InjectView(R.id.bottomProgress)
    RelativeLayout bottomProgress;
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
    private LinearLayoutManager layoutManager;
    private GetNewsInteractor interactor;
    private GetJagatPlayInteractor playInteractor;
    private GetJagatOcInteractor ocInteractor;
    private ArticleAdapter adapter;
    private boolean loading;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, page, type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);
        page = 1;
        type = 1;
        loading = false;

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
        layoutManager = new LinearLayoutManager(this);
        adapter = new ArticleAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        Article article = adapter.getItemData(position);
                        intent.putExtra("article", article);
                        startActivity(intent);
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
            public void onScrollStateChanged(RecyclerView recyclerView,int newState)
            {
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (totalItemCount> 1)
                {
                    if (lastVisibleItem >= totalItemCount - 1)
                    {
                        if(!loading) {
                            page++;
                            getMoreFeed(type, page);
                        }
                    }
                }
            }
        });
        getFeed(1);
    }

    private void setJagatReview() {
        toolbar.setTitle(R.string.title_jagat_review);
        type = 1;
        page = 1;
        getFeed(type);
    }

    private void setJagatPlay() {
        toolbar.setTitle(R.string.title_jagat_play);
        type = 2;
        page = 1;
        getFeed(type);
    }

    private void setJagatOc() {
        toolbar.setTitle(R.string.title_jagat_oc);
        type = 3;
        page = 1;
        getFeed(type);
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
        loading = true;
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        switch (type) {
            case 1:
                interactor.execute(1, new GetNewsInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        List<Article> articles = new ArrayList<Article>();
                        for (Item item : rss.getChannel().getItem()) {
                            Article article = new Article();
                            article.setTitle(item.getTitle());
                            article.setDateTime(item.getPubDate());
                            article.setContent(item.getContent());
                            article.setCreator(item.getCreator());
                            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
                            Matcher matcher = pattern.matcher(item.getContent());
                            if(matcher.find()) {
                                if (matcher.group(0) != null) {
                                    article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                                }
                            }
                            articles.add(article);
                        }
                        adapter.setArticles(articles);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loading = false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loading = false;
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
                            article.setContent(item.getContent());
                            article.setCreator(item.getCreator());
                            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
                            Matcher matcher = pattern.matcher(item.getContent());
                            if(matcher.find()) {
                                if (matcher.group(0) != null) {
                                    article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                                }
                            }
                            articles.add(article);
                        }
                        adapter.setArticles(articles);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loading = false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loading = false;
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
                            article.setDateTime(item.getPubDate());;
                            article.setContent(item.getContent());
                            article.setCreator(item.getCreator());
                            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
                            Matcher matcher = pattern.matcher(item.getContent());
                            if(matcher.find()) {
                                if (matcher.group(0) != null) {
                                    article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                                }
                            }
                            articles.add(article);
                        }
                        adapter.setArticles(articles);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loading = false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loading = false;
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

    private void getMoreFeed(int type, int page) {
        loading = true;
        bottomProgress.setVisibility(View.VISIBLE);
        switch (type) {
            case 1:
                interactor.execute(page, new GetNewsInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        List<Article> articles = new ArrayList<Article>();
                        for (Item item: rss.getChannel().getItem()) {
                            Article article = new Article();
                            article.setTitle(item.getTitle());
                            article.setDateTime(item.getPubDate());
                            article.setContent(item.getContent());
                            article.setCreator(item.getCreator());
                            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
                            Matcher matcher = pattern.matcher(item.getContent());
                            if(matcher.find()) {
                                if (matcher.group(0) != null) {
                                    article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                                }
                            }
                            articles.add(article);
                        }
                        adapter.addArticles(articles);
                        adapter.notifyDataSetChanged();
                        bottomProgress.setVisibility(View.GONE);
                        loading = false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loading = false;
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(mainContainer, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                playInteractor.execute(page, new GetJagatPlayInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        List<Article> articles = new ArrayList<Article>();
                        for (Item item: rss.getChannel().getItem()) {
                            Article article = new Article();
                            article.setTitle(item.getTitle());
                            article.setDateTime(item.getPubDate());
                            article.setContent(item.getContent());
                            article.setCreator(item.getCreator());
                            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
                            Matcher matcher = pattern.matcher(item.getContent());
                            if(matcher.find()) {
                                if (matcher.group(0) != null) {
                                    article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                                }
                            }
                            articles.add(article);
                        }
                        adapter.addArticles(articles);
                        adapter.notifyDataSetChanged();
                        bottomProgress.setVisibility(View.GONE);
                        loading = false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loading = false;
                        bottomProgress.setVisibility(View.GONE);
                        Snackbar.make(mainContainer, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            case 3:
                ocInteractor.execute(page, new GetJagatOcInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        List<Article> articles = new ArrayList<Article>();
                        for (Item item: rss.getChannel().getItem()) {
                            Article article = new Article();
                            article.setTitle(item.getTitle());
                            article.setDateTime(item.getPubDate());
                            article.setContent(item.getContent());
                            article.setCreator(item.getCreator());
                            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
                            Matcher matcher = pattern.matcher(item.getContent());
                            if(matcher.find()) {
                                if (matcher.group(0) != null) {
                                    article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                                }
                            }
                            articles.add(article);
                        }
                        adapter.addArticles(articles);
                        adapter.notifyDataSetChanged();
                        bottomProgress.setVisibility(View.GONE);
                        loading = false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loading = false;
                        bottomProgress.setVisibility(View.GONE);
                        Snackbar.make(mainContainer, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            default:
                break;
        }
    }
}
