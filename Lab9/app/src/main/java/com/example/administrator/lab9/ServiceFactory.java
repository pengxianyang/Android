package com.example.administrator.lab9;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/12/21.
 */

public class ServiceFactory {
    public static  String API_URL = "https://api.github.com";
    public interface GithubService
    {
        @GET("/users/{user}")
        Observable<Github> getUser(@Path("user") String user);

        @GET("/users/{user}/repos")
        Observable<List<Repositories>> getUserRepos(@Path("user") String user);
    }

    public static Retrofit createRetrofit()
    {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(50,TimeUnit.SECONDS);
        builder.connectTimeout(50,TimeUnit.SECONDS);

        if(BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return new Retrofit.Builder().baseUrl(API_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }
}
