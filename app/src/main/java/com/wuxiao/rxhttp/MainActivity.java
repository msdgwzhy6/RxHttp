package com.wuxiao.rxhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wuxiao.rxhttp.cache.CacheProviders;
import com.wuxiao.rxhttp.cache.RxCache;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RxCache rxCache = RetrofitHelper.getRxCache(this);
        RxNetwork.getInstance()
                .createApi(Api.class, false)
                .getCategoryData(1)
                .compose(rxCache.<CategoryData>transformer("cache", CacheProviders.cache))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<CategoryData>() {
                    @Override
                    public void onSuccess(CategoryData adBeanResult) {
                        Log.i("MainActivity", adBeanResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable e) {

                    }
                });

    }
}
