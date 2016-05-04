package com.github.rakawestu.jagatreader.ui.presenter;

import com.github.rakawestu.jagatreader.domain.interactor.GetJagatGadgetInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatGadgetInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcCompetitionInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcCompetitionInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcReviewInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatOcReviewInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayGearInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayGearInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayNintendoInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayNintendoInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayPCInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayPCInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayPSInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayPSInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayTopInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatPlayTopInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatReleaseInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatReleaseInteractorImpl;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatReviewInteractor;
import com.github.rakawestu.jagatreader.domain.interactor.GetJagatReviewInteractorImpl;
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

    public static final int TYPE_REVIEW_LATEST = 1;
    public static final int TYPE_PLAY_LATEST = 2;
    public static final int TYPE_OC_LATEST = 3;
    public static final int TYPE_REVIEW_REVIEW = 4;
    public static final int TYPE_REVIEW_GADGET = 5;
    public static final int TYPE_REVIEW_RELEASE = 6;
    public static final int TYPE_PLAY_PC = 8;
    public static final int TYPE_PLAY_PS = 9;
    public static final int TYPE_PLAY_NINTENDO = 10;
    public static final int TYPE_PLAY_GEAR = 11;
    public static final int TYPE_PLAY_TOP = 12;
    public static final int TYPE_OC_REVIEW = 13;
    public static final int TYPE_OC_COMPETITION = 14;

    private NewsView view;
    private GetNewsInteractor interactor;
    private GetJagatOcInteractor ocInteractor;
    private GetJagatPlayInteractor playInteractor;
    private GetJagatReviewInteractor reviewInteractor;
    private GetJagatGadgetInteractor gadgetInteractor;
    private GetJagatReleaseInteractor releaseInteractor;
    private GetJagatPlayPCInteractor playPCInteractor;
    private GetJagatPlayPSInteractor playPSInteractor;
    private GetJagatPlayNintendoInteractor nintendoInteractor;
    private GetJagatPlayGearInteractor gearInteractor;
    private GetJagatPlayTopInteractor topInteractor;
    private GetJagatOcReviewInteractor ocReviewInteractor;
    private GetJagatOcCompetitionInteractor ocCompetitionInteractor;
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
        reviewInteractor = new GetJagatReviewInteractorImpl(threadExecutor, mainThreadExecutor);
        gadgetInteractor = new GetJagatGadgetInteractorImpl(threadExecutor, mainThreadExecutor);
        releaseInteractor = new GetJagatReleaseInteractorImpl(threadExecutor, mainThreadExecutor);
        playPCInteractor = new GetJagatPlayPCInteractorImpl(threadExecutor, mainThreadExecutor);
        playPSInteractor = new GetJagatPlayPSInteractorImpl(threadExecutor, mainThreadExecutor);
        nintendoInteractor = new GetJagatPlayNintendoInteractorImpl(threadExecutor, mainThreadExecutor);
        gearInteractor = new GetJagatPlayGearInteractorImpl(threadExecutor, mainThreadExecutor);
        topInteractor = new GetJagatPlayTopInteractorImpl(threadExecutor, mainThreadExecutor);
        ocReviewInteractor = new GetJagatOcReviewInteractorImpl(threadExecutor, mainThreadExecutor);
        ocCompetitionInteractor = new GetJagatOcCompetitionInteractorImpl(threadExecutor, mainThreadExecutor);
        page = 1;
        type = TYPE_REVIEW_LATEST;
        articles = new ArrayList<>();
    }

    public NewsPresenterImpl(int type) {
        threadExecutor = new ThreadExecutor();
        mainThreadExecutor = new MainThreadExecutorImp();
        interactor = new GetNewsInteractorImpl(threadExecutor, mainThreadExecutor);
        ocInteractor = new GetJagatOcInteractorImpl(threadExecutor, mainThreadExecutor);
        playInteractor = new GetJagatPlayInteractorImpl(threadExecutor, mainThreadExecutor);
        reviewInteractor = new GetJagatReviewInteractorImpl(threadExecutor, mainThreadExecutor);
        gadgetInteractor = new GetJagatGadgetInteractorImpl(threadExecutor, mainThreadExecutor);
        releaseInteractor = new GetJagatReleaseInteractorImpl(threadExecutor, mainThreadExecutor);
        playPCInteractor = new GetJagatPlayPCInteractorImpl(threadExecutor, mainThreadExecutor);
        playPSInteractor = new GetJagatPlayPSInteractorImpl(threadExecutor, mainThreadExecutor);
        nintendoInteractor = new GetJagatPlayNintendoInteractorImpl(threadExecutor, mainThreadExecutor);
        gearInteractor = new GetJagatPlayGearInteractorImpl(threadExecutor, mainThreadExecutor);
        topInteractor = new GetJagatPlayTopInteractorImpl(threadExecutor, mainThreadExecutor);
        ocReviewInteractor = new GetJagatOcReviewInteractorImpl(threadExecutor, mainThreadExecutor);
        ocCompetitionInteractor = new GetJagatOcCompetitionInteractorImpl(threadExecutor, mainThreadExecutor);
        page = 1;
        this.type = type;
        articles = new ArrayList<>();
    }

    @Override
    public void onNews(Article article) {
        view.showDetails(article);
    }

    @Override
    public void onRefresh() {
        page = 1;
        refreshData(type, page);
    }

    @Override
    public void loadMore() {
        page++;
        loadData(type, page);
    }

    @Override
    public void changeType(int type) {
        view.hideContent();
        this.type = type;
        page = 1;
        refreshData(this.type, page);
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
            case TYPE_REVIEW_LATEST:
                //Load jagat review news
                interactor.execute(page, new GetNewsInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        if (rss.getChannel().getItem() != null && rss.getChannel().getItem().size() > 0) {
                            articles.addAll(extractArticle(rss.getChannel().getItem()));
                            view.addData(articles);
                        } else {
                            view.onError(new Exception("No items"));

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_PLAY_LATEST:
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
            case TYPE_OC_LATEST:
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
            case TYPE_REVIEW_REVIEW:
                reviewInteractor.execute(page, new GetJagatReviewInteractor.Callback() {
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
            case TYPE_REVIEW_GADGET:
                gadgetInteractor.execute(page, new GetJagatGadgetInteractor.Callback() {
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
            case TYPE_REVIEW_RELEASE:
                releaseInteractor.execute(page, new GetJagatReleaseInteractor.Callback() {
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
            case TYPE_PLAY_PC:
                playPCInteractor.execute(page, new GetJagatPlayPCInteractor.Callback() {
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
            case TYPE_PLAY_PS:
                playPSInteractor.execute(page, new GetJagatPlayPSInteractor.Callback() {
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
            case TYPE_PLAY_NINTENDO:
                nintendoInteractor.execute(page, new GetJagatPlayNintendoInteractor.Callback() {
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
            case TYPE_PLAY_GEAR:
                gearInteractor.execute(page, new GetJagatPlayGearInteractor.Callback() {
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
            case TYPE_PLAY_TOP:
                topInteractor.execute(page, new GetJagatPlayTopInteractor.Callback() {
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
            case TYPE_OC_COMPETITION:
                ocCompetitionInteractor.execute(page, new GetJagatOcCompetitionInteractor.Callback() {
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
            case TYPE_OC_REVIEW:
                ocReviewInteractor.execute(page, new GetJagatOcReviewInteractor.Callback() {
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

    private void refreshData(int type, int page) {
        view.showLoading();

        switch (type) {
            case TYPE_REVIEW_LATEST:
                //Load jagat review news
                interactor.execute(page, new GetNewsInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_PLAY_LATEST:
                //Load jagat play news
                playInteractor.execute(page, new GetJagatPlayInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_OC_LATEST:
                //Load jagat oc news
                ocInteractor.execute(page, new GetJagatOcInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_REVIEW_REVIEW:
                //Load jagat review news
                reviewInteractor.execute(page, new GetJagatReviewInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_REVIEW_GADGET:
                //Load jagat gadget news
                gadgetInteractor.execute(page, new GetJagatGadgetInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_REVIEW_RELEASE:
                //Load jagat release news
                releaseInteractor.execute(page, new GetJagatReleaseInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_PLAY_PC:
                playPCInteractor.execute(page, new GetJagatPlayPCInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_PLAY_PS:
                playPSInteractor.execute(page, new GetJagatPlayPSInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_PLAY_NINTENDO:
                nintendoInteractor.execute(page, new GetJagatPlayNintendoInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_PLAY_GEAR:
                gearInteractor.execute(page, new GetJagatPlayGearInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_PLAY_TOP:
                topInteractor.execute(page, new GetJagatPlayTopInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_OC_COMPETITION:
                ocCompetitionInteractor.execute(page, new GetJagatOcCompetitionInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.hideLoading();
                        view.onError(throwable);
                    }
                });
                break;
            case TYPE_OC_REVIEW:
                ocReviewInteractor.execute(page, new GetJagatOcReviewInteractor.Callback() {
                    @Override
                    public void onRss(Rss rss) {
                        view.hideLoading();
                        articles.clear();
                        articles.addAll(extractArticle(rss.getChannel().getItem()));
                        view.addData(articles);
                        view.showContent();
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
            Article article = new Article(item.getTitle(), item.getDescription(), item.getPubDate(), "", item.getContent(), item.getCreator(), item.getLink(), item.getCategory().get(0));
            Pattern pattern = Pattern.compile("src=\"(.+?)\"");
            if(item.getContent()!= null && !item.getContent().equals("")) {
                Matcher matcher = pattern.matcher(item.getContent());
                if (matcher.find()) {
                    if (matcher.group(0) != null) {
                        article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                    }
                }
            } else if(item.getDescription()!=null&&!item.getDescription().equals("")) {
                Matcher matcher = pattern.matcher(item.getDescription());
                if (matcher.find()) {
                    if (matcher.group(0) != null) {
                        article.setImageUrl(matcher.group(0).replace("src=\"", "").replace("\"", ""));
                    }
                }
            }
            articles.add(article);
        }
        return articles;
    }
}
