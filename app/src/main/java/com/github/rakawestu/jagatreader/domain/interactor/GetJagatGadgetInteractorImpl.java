package com.github.rakawestu.jagatreader.domain.interactor;

import com.github.rakawestu.jagatreader.domain.repository.MainRepository;
import com.github.rakawestu.jagatreader.domain.repository.api.retrofit.NewsFeedRepository;
import com.github.rakawestu.jagatreader.executor.InteractorExecutor;
import com.github.rakawestu.jagatreader.executor.MainThreadExecutor;
import com.github.rakawestu.jagatreader.model.news.Rss;

import timber.log.Timber;

/**
 * @author rakawm
 */
public class GetJagatGadgetInteractorImpl extends AbstractInteractor implements GetJagatGadgetInteractor {

    private int page;
    private Callback callback;
    private MainRepository repository;

    public GetJagatGadgetInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor) {
        super(interactorExecutor, mainThreadExecutor);
        this.repository = new NewsFeedRepository("http://www.jagatreview.com/category/gadget/");
    }

    @Override
    public void execute(int page, Callback callback) {
        this.page = page;
        this.callback = callback;

        getInteractorExecutor().run(this);
    }

    @Override
    public void run() {
        try {
            final Rss rss = repository.getRss(page);
            getMainThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onRss(rss);
                }
            });
        } catch (final Exception e) {
            Timber.e(e, "Error on get news interactor.");
            getMainThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onError(e);
                }
            });
        }
    }
}
