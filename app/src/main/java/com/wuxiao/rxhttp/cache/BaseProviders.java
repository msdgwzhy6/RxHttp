package com.wuxiao.rxhttp.cache;

import com.wuxiao.rxhttp.cache.data.Result;
import com.wuxiao.rxhttp.cache.data.ResultFrom;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *Created by wuxiao on 2017/1/10.
 */
abstract class BaseProviders  {


    <T> Observable<Result<T>> loadCache(final RxCache rxCache, final String key) {
        return rxCache
                .<T>load(key)
                .map(new Func1<T, Result<T>>() {
                    @Override
                    public Result<T> call(T o) {
                        return new Result<>(ResultFrom.Cache, key,  o);
                    }
                });
    }

     <T> Observable<Result<T>> loadRemote(final RxCache rxCache, final String key, Observable<T> source, final CacheType target) {
        return source
                .map(new Func1<T, Result<T>>() {
                    @Override
                    public Result<T> call(T t) {
                        rxCache.save(key, t,target).subscribeOn(Schedulers.io())
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean status) {

                                    }
                                });
                        return new Result<>(ResultFrom.Remote, key, t);
                    }
                });
    }


}
