package com.github.rakawestu.jagatreader.domain.interactor;

import com.github.rakawestu.jagatreader.model.news.Rss;

/**
 * @author rakawm
 */
public interface GetJagatPlayNintendoInteractor {
    void execute(int page, Callback callback);

    interface Callback {
        void onRss(Rss rss);

        void onError(Throwable throwable);
    }
}
