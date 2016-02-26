package com.github.rakawestu.jagatreader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.model.Article;
import com.github.rakawestu.jagatreader.ui.activity.DetailActivity;
import com.github.rakawestu.jagatreader.ui.adapter.ArticleAdapter;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenter;
import com.github.rakawestu.jagatreader.ui.presenter.NewsPresenterImpl;
import com.github.rakawestu.jagatreader.ui.view.NewsView;
import com.github.rakawestu.jagatreader.utils.RecyclerViewItemClickListener;

import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author rakawm
 */
public class NewsListFragment extends Fragment implements NewsView {
    public static final String ARG_TYPE = "type";

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsPresenter presenter;
    private ArticleAdapter adapter;
    private SlideInBottomAnimationAdapter animationAdapter;
    private LinearLayoutManager layoutManager;
    private boolean loading;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstaceState) {
        super.onViewCreated(view, savedInstaceState);
        presenter = new NewsPresenterImpl(getArguments().getInt(ARG_TYPE));
        presenter.setView(this);
        layoutManager = new LinearLayoutManager(getContext());
        resetAdapter();
        setupRecyclerView();
        //Swipe Refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.onViewCreate();
            }
        }, 300);
    }

    private void resetAdapter() {
        adapter = new ArticleAdapter(getContext());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), new RecyclerViewItemClickListener.OnItemClickListener() {
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
        Intent intent = new Intent(getContext(), DetailActivity.class);
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
        Snackbar.make(recyclerView, "Gagal mengambil berita.", Snackbar.LENGTH_INDEFINITE).setAction(R.string.action_retry, new View.OnClickListener() {
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
}
