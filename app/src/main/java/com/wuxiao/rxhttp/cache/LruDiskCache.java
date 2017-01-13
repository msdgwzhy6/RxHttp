package com.wuxiao.rxhttp.cache;


import com.jakewharton.disklrucache.DiskLruCache;
import com.wuxiao.rxhttp.cache.diskconverter.IDiskConverter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by wuxiao on 2017/1/10.
 */
class LruDiskCache {
    private IDiskConverter mDiskConverter;
    private DiskLruCache mDiskLruCache;


     LruDiskCache(IDiskConverter diskConverter, File diskDir, int appVersion, long diskMaxSize) {
        this.mDiskConverter = diskConverter;
        try {
            mDiskLruCache = DiskLruCache.open(diskDir, appVersion, 1, diskMaxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     <T> T load(String key) {
        if (mDiskLruCache == null) {
            return null;
        }
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit == null) {
                return null;
            }
            InputStream source = edit.newInputStream(0);
            T value ;
            if (source != null) {
                value = (T) mDiskConverter.load(source);
                close(source);
                edit.commit();
                return value;
            }
            edit.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

     <T> boolean save(String key, T value) {
        if (mDiskLruCache == null) {
            return false;
        }
        //如果要保存的值为空,则删除
        if (value == null) {
            return remove(key);
        }
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit == null) {
                return false;
            }
            OutputStream sink = edit.newOutputStream(0);
            if (sink != null) {
                mDiskConverter.writer(sink, value);
                close(sink);
                edit.commit();
                return true;
            }
            edit.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

     boolean containsKey(String key) {
        try {
            return mDiskLruCache.get(key) != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除缓存
     *
     */
     final boolean remove(String key) {
        try {
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

     void clear() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void close(Closeable close) {
        if (close != null) {
            try {
                closeThrowException(close);
            } catch (IOException ignored) {
            }
        }
    }

    public  void closeThrowException(Closeable close) throws IOException {
        if (close != null) {
            close.close();
        }
    }

}
