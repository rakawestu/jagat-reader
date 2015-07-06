package com.github.rakawestu.jagatreader.domain.repository.api.retrofit;

import com.github.rakawestu.jagatreader.domain.repository.MainRepository;
import com.github.rakawestu.jagatreader.domain.repository.api.retrofit.service.NewsService;
import com.github.rakawestu.jagatreader.model.news.Rss;

import retrofit.RetrofitError;

/**
 * @author rakawm
 */
public class NewsFeedRepository extends BaseRepository implements MainRepository {
    private NewsService service;

    public NewsFeedRepository(String endpoint) {
        this.endpoint = endpoint;

        createAdapter();
        service = adapter.create(NewsService.class);
    }

    @Override
    public Rss getRss(int page) throws Exception{
        try {
            Rss rss = service.getPagedNews(page);
            return rss;
        } catch (RetrofitError e) {
            throw e;
        }
    }
}
