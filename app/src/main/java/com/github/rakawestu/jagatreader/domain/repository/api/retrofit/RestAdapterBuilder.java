package com.github.rakawestu.jagatreader.domain.repository.api.retrofit;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.SimpleXMLConverter;

/**
 * Rest Adapter Builder class.
 *
 * @author rakawm
 */
public class RestAdapterBuilder {

    /**
     * Create rest adapter using retrofit library.
     * @param endpoint       api base url.
     * @return rest adapter for call the Api.
     */
    public static RestAdapter createAdapter(String endpoint){
        RestAdapter adapter = new RestAdapter.Builder()
                .setClient(new OkClient(createClient()))
                .setConverter(new SimpleXMLConverter())
                .setEndpoint(endpoint)
                .build();
        return adapter;
    }

    public static OkHttpClient createClient(){
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        return client;
    }
}
