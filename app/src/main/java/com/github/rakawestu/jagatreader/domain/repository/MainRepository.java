package com.github.rakawestu.jagatreader.domain.repository;


import com.github.rakawestu.jagatreader.model.news.Rss;

/**
 * @author rakawm
 */
public interface MainRepository {

    Rss getRss(int page) throws Exception;

}
