package com.github.rakawestu.jagatreader.domain.interactor;

import com.github.rakawestu.jagatreader.model.news.Rss;

/**
 * Created by rakawm on 2/25/16.
 */
public interface GetJagatReviewInteractor {
    void execute(int page, Callback callback);

    interface Callback {
        void onRss(Rss rss);

        void onError(Throwable throwable);
    }
}
