package com.wuxiao.rxhttp;


import com.wuxiao.rxhttp.cache.data.Result;

import rx.Subscriber;
/**
 * Created by wuxiao on 2017/1/10.
 */
public abstract class RxSubscriber<T> extends Subscriber<Result<T>> {

    @Override
    public void onCompleted() {
        // 忽略操作，需要可覆写该方法
    }

    @Override
    public void onError(Throwable e) {
        onFailed(e);
        e.printStackTrace();
    }

    @Override
    public void onNext(Result<T> t) {
      /*  if (t.data instanceof ResponseData) {
            ResponseData response = (ResponseData) t.data;
            // 判断是否请求错误，出错直接转到onError()
            if (!response.isSuccess()) {
                this.onError(new ServerException(response.getMessage()));
                return;
            }
        }*/

        onSuccess(t.data);

    }

    /**
     * 请求成功回调
     *
     * @param t 最终响应结果
     */
    public abstract void onSuccess(T t);

    public abstract void onFailed(Throwable e);
}