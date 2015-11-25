package com.example.alexandra.popularmovies.utils;


import com.example.alexandra.popularmovies.BuildConfig;
import com.example.alexandra.popularmovies.network.MainApi;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Provider;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

/**
 * RoboGuice module for the app.
 */
public class ApplicationModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(MainApi.class).toProvider(ConnectionsProvider.class);
        bind(RequestInterceptor.class).toProvider(RequestInterceptorProvider.class);
    }


    private static class RequestInterceptorProvider
            implements Provider<RequestInterceptor> {
        @Override
        public RequestInterceptor get() {
            return new RequestInterceptor() {
                @Override
                public void intercept(RequestInterceptor.RequestFacade request) {

                }
            };
        }
    }

    private static class ConnectionsProvider
            implements Provider<MainApi> {

        @Inject
        public ConnectionsProvider() {
        }

        @Override
        public MainApi get() {

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(
                    30,
                    TimeUnit.SECONDS
            );
            client.setReadTimeout(
                    120,
                    TimeUnit.SECONDS
            );

            final RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(
                            BuildConfig.ENDPOINT
                    )
                    .setClient(new OkClient(client)
                    )
                    .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("API_GETS"))
                    .build();
            return restAdapter.create(MainApi.class);
        }
    }



}
