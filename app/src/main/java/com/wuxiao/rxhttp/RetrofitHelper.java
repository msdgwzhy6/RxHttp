package com.wuxiao.rxhttp;


import android.content.Context;
import android.util.Log;

import com.wuxiao.rxhttp.cache.RxCache;
import com.wuxiao.rxhttp.cache.diskconverter.DiskConverter;
import com.wuxiao.rxhttp.interceptor.CommonInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitHelper {

    private static Retrofit.Builder retrofitBuilder;

    public static final String baseUrl = " http://gank.io/api/search/query/listview/";

    private static HttpLoggingInterceptor loggingInterceptor;

    private static CommonInterceptor commonInterceptor;

    public static Retrofit getInstance(boolean isCommon) {

        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

                @Override
                public void log(String message) {
                    Log.i("retrofit:", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        }


        commonInterceptor = new CommonInterceptor(isCommon);

        retrofitBuilder.baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(getClient());

        return retrofitBuilder.build();
    }

    public static OkHttpClient getClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(commonInterceptor)
                .addInterceptor(loggingInterceptor)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }
    public static RxCache getRxCache(Context context) {

        RxCache rxCache = new RxCache.Builder()
                .diskDir(new File(context.getCacheDir().getPath() + File.separator + "data"))
                .diskConverter(new DiskConverter())
                .memorySize(2*1024*1024)
                .build();
       return rxCache;
    }
}
