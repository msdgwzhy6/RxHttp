package com.wuxiao.rxhttp.cache;

import android.os.StatFs;

import com.wuxiao.rxhttp.cache.data.Result;
import com.wuxiao.rxhttp.cache.diskconverter.DiskConverter;
import com.wuxiao.rxhttp.cache.diskconverter.IDiskConverter;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;


/**
 * Created by wuxiao on 2017/1/10.
 * RxJava数据缓存处理，目前只支持Serializable
 */
public final class RxCache {


    private final CacheCore cacheCore;

    private RxCache(int memoryMaxSize, int appVersion, long diskMaxSize, File diskDir, IDiskConverter diskConverter) {
        cacheCore = new CacheCore(new LruMemoryCache(memoryMaxSize), new LruDiskCache(diskConverter,diskDir,appVersion,diskMaxSize));
    }

    public <T> Observable.Transformer<T, Result<T>> transformer(final String key, final CacheProviders providers) {
        return new Observable.Transformer<T, Result<T>>() {
            @Override
            public Observable<Result<T>> call(Observable<T> tObservable) {
                return providers.execute(RxCache.this,key,tObservable);
            }
        };
    }

    private static abstract class SimpleSubscribe<T> implements rx.Observable.OnSubscribe<T> {
        @Override
        public final void call(Subscriber<? super T> subscriber) {
            try {
                T data = execute();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(data);
                }
            } catch (Throwable e) {

                Exceptions.throwIfFatal(e);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
                return;
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }

        abstract T execute() throws Throwable;
    }

    /**
     * 读取
     */
    public <T> rx.Observable<T> load(final String key) {
        return rx.Observable.create(new SimpleSubscribe<T>() {
            @Override
            T execute() {
                return cacheCore.load(key);
            }
        });
    }

    /**
     * 保存
     */
    public <T> rx.Observable<Boolean> save(final String key, final T value, final CacheType target) {
        return rx.Observable.create(new SimpleSubscribe<Boolean>() {
            @Override
            Boolean execute() throws Throwable {
                cacheCore.save(key, value, target);
                return true;
            }
        });
    }

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    public boolean containsKey(final String key) {
        return cacheCore.containsKey(key);
    }

    /**
     * 删除缓存
     *
     */
    public boolean remove(final String key) {
        return cacheCore.remove(key);
    }

    /**
     * 清空缓存
     */
    public rx.Observable<Boolean> clear() {
        return rx.Observable.create(new SimpleSubscribe<Boolean>() {
            @Override
            Boolean execute() throws Throwable {
                cacheCore.clear();
                return true;
            }
        });
    }

    /**
     * 构造器
     */
    public static final class Builder {
        private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
        private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
        private static final int DEFAULT_MEMORY_CACHE_SIZE=(int) (Runtime.getRuntime().maxMemory()/8);//运行内存的8分之1
        private int memoryMaxSize;
        private int appVersion;
        private long diskMaxSize;
        private File diskDir;
        private IDiskConverter diskConverter;

        public Builder(){
        }

        /**
         * 不设置,默认为运行内存的8分之1
         */
        public Builder memorySize(int maxSize) {
            this.memoryMaxSize = maxSize;
            return this;
        }

        /**
         * 不设置，默认为1
         */
        public Builder appVersion(int appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        public Builder diskDir(File directory) {
            this.diskDir = directory;
            return this;
        }


        public Builder diskConverter(IDiskConverter converter) {
            this.diskConverter = converter;
            return this;
        }

        /**
         * 不设置， 默为认50MB
         */
        public Builder diskSize(long maxSize) {
            this.diskMaxSize = maxSize;
            return this;
        }
        public RxCache build() {
            if(this.diskDir==null){
                throw new NullPointerException("DiskDir can not be null");
            }
            if (!this.diskDir.exists()) {
               this.diskDir.mkdirs();
            }
            if(this.diskConverter==null){
                this.diskConverter=new DiskConverter();
            }
            if(memoryMaxSize<=0){
                memoryMaxSize= DEFAULT_MEMORY_CACHE_SIZE;
            }
            if(diskMaxSize<=0){
                diskMaxSize=measureDiskCacheSize(diskDir);

            }
            appVersion= Math.max(1,this.appVersion);
            return  new RxCache(memoryMaxSize,appVersion,diskMaxSize,diskDir,diskConverter);
        }

        private static long measureDiskCacheSize(File dir) {
            long size = 0;

            try {
                StatFs statFs = new StatFs(dir.getAbsolutePath());
                long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
                // Target 2% of the total space.
                size = available / 50;
            } catch (IllegalArgumentException ignored) {
            }
            // Bound inside min/max size for disk cache.
            return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
        }

    }




}
