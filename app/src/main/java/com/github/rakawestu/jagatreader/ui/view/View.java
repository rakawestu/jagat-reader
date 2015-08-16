package com.github.rakawestu.jagatreader.ui.view;

/**
 * Base view interface.
 *
 * @author rakawm
 */
public interface View<T> {
    void showLoading();

    void hideLoading();

    void addData(T data);

    void onError(Throwable throwable);
}
