package com.github.rakawestu.jagatreader.domain.repository.api.retrofit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * @author rakawm
 */
public abstract class BaseRepository {
    public String endpoint;
    public RequestInterceptor requestInterceptor;
    public RestAdapter adapter;

    public void createAdapter(){
        adapter = RestAdapterBuilder.createAdapter(endpoint);
    }
}
