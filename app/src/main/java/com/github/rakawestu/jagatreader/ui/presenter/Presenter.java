package com.github.rakawestu.jagatreader.ui.presenter;

import com.github.rakawestu.jagatreader.ui.view.View;

/**
 * @author rakawm
 */
public interface Presenter<T extends View> {
    void onViewCreate();

    void onViewResume();

    void onViewDestroy();

    void setView(T view);
}
