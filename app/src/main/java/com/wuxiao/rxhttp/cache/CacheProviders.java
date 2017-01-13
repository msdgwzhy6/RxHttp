package com.wuxiao.rxhttp.cache;


import com.wuxiao.rxhttp.cache.data.Result;

import rx.Observable;
import rx.functions.Func1;


 public final class CacheProviders extends BaseProviders {
    private CacheProviders(){}

    public static final CacheProviders cache=new CacheProviders();

    public <T> Observable<Result<T>> execute(RxCache rxCache, String key, Observable<T> source) {
        Observable<Result<T>> cache = loadCache(rxCache,key);
        Observable<Result<T>> remote = loadRemote(rxCache,key, source, CacheType.MemoryAndDisk)
                .onErrorReturn(new Func1<Throwable, Result<T>>() {
                    @Override
                    public Result<T> call(Throwable throwable) {
                        return null;
                    }
                });
        return Observable.concat(remote, cache)
                .firstOrDefault(null, new Func1<Result<T>, Boolean>() {
                    @Override
                    public Boolean call(Result<T> tResultData) {
                        return tResultData != null && tResultData.data != null;
                    }
                });

    }
}
