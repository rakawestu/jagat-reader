package com.github.rakawestu.jagatreader.ui.view;

import com.github.rakawestu.jagatreader.model.Article;

import java.util.List;

/**
 * @author rakawm
 */
public interface NewsView extends View<List<Article>> {
    void showDetails(Article article);

    void showContent();

    void hideContent();
}
