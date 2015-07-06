package com.github.rakawestu.jagatreader.domain.repository.api.retrofit.service;

import com.github.rakawestu.jagatreader.model.news.Rss;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author rakawm
 */
public interface NewsService {
    /**
     * Get main news feed.
     * @param page    page number
     * @return RSS feed.
     */
    @GET("/feed")
    Rss getPagedNews(
            @Query("paged") int page
    );
}
