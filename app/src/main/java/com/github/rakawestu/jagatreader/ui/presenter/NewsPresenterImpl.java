package com.github.rakawestu.jagatreader.ui.presenter;

import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetNewsInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetNewsInteractorImpl;
import com.github.rakawestu.jagatreader.executor.InteractorExecutor;
import com.github.rakawestu.jagatreader.executor.MainThreadExecutor;
import com.github.rakawestu.jagatreader.executor.MainThreadExecutorImp;
import com.github.rakawestu.jagatreader.executor.ThreadExecutor;
import com.github.rakawestu.jagatreader.model.Article;
import com.github.rakawestu.jagatreader.model.news.Item;
import com.github.rakawestu.jagatreader.model.news.Rss;
import com.github.rakawestu.jagatreader.ui.view.NewsView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rakawm
 */
public class NewsPresenterImpl implements NewsPresenter {

    private NewsView view;
    private GetNewsInteractor interactor;
    private GetJagatOcInteractor ocInteractor;
    private GetJagatPlayInteractor playInteractor;
    private InteractorExecutor threadExecutor;
    private MainThreadExecutor mainThreadExecutor;
    private List<Article> articles;
    private int page, type;

    public NewsPresenterImpl() {
        threadExecutor = new ThreadExecutor();
        mainThreadExecutor = new MainThreadExecutorImp();
        interactor = new GetNewsInteractorImpl(threadExecutor, mainThreadExecutor);
        ocInteractor = new GetJagatOcInteractorImpl(threadExecutor, mainThreadExecutor);
        playInteractor = new GetJagatPlayInteractorImpl(threadExecutor, mainThreadExecutor);

        page = 1;
        type = 1;
        articles = new ArrayList<>();
    }

    @Override
    public void onNews(Article article) {
        view.showDetails(article);
    }

    @Override
    public void onRefresh() {
        page = 1;
        articles.clear();
        loadData(type, page);
    }

    @Override
    public void loadMore() {
        page++;
        loadData(type, page);
    }

    @Override
    public void changeType(int type) {
        this.type = type;
        page = 1;
        articles.clear();
        loadData(this.type, page);
    }

    @Override
    public void onViewCreate() {
        loadData(type, page);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void setView(NewsView view) {
        this.view = view;
    }

    private void loadData(int type, int page) {
        view.showLoading();

        switch (type) {
            case 1:
                //Load jagat review news
                interactor.execute(page, new GetNewsInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case 2:
                //Load jagat play news
                playInteractor.execute(page, new GetJagatPlayInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case 3:
                //Load jagat oc news
                ocInteractor.execute(page, new GetJagatOcInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
        }
    }

    private List<Article> extractArticle(List<Item> items) {
        List<Article> articles = new ArrayList<>();
        for (Item item : items) {
            Article article = new Article(item.getTitle(), item.getDescription(), item.getPubDate(), "", item.getContent(), item.getCreator(), item.getLink());
            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
            Matcher matcher = pattern.matcher(item.getContent());
            if (matcher.find()) {
                if (matcher.group(0) != null) {
                    article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                }
            }
            articles.add(article);
        }
        return articles;
    }
}
