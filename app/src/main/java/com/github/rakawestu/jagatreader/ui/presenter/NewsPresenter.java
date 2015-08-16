package com.github.rakawestu.jagatreader.ui.presenter;

import com.github.rakawestu.jagatreader.model.Article;
import com.github.rakawestu.jagatreader.ui.view.NewsView;

/**
 * @author rakawm
 */
public interface NewsPresenter extends Presenter<NewsView> {

    void onNews(Article article);

    void onRefresh();

    void loadMore();

    void changeType(int type);
}
