package com.example.administrator.lab6;

import java.util.List;
import java.util.concurrent.TimeUnit;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import com.example.administrator.lab6.BuildConfig;
import com.example.administrator.lab6.Github;
import com.example.administrator.lab6.Repositories;
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
    public static  String API_URL = "https://developer.github.com/v3/";
    public interface GithubService
    {
       @GET("/users/{user}")
       Observable<Github> getUser(@Path("user") String user);
    }

    public static Retrofit createRetrofit()
    {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(20,TimeUnit.SECONDS);
        builder.connectTimeout(20,TimeUnit.SECONDS);

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
