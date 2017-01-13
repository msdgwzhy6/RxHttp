package com.wuxiao.rxhttp.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wuxiao on 2017/1/10.
 * okHttp请求拦截器统一添加请求头
 */
public class CommonInterceptor implements Interceptor {

    private boolean isCommon;


    public CommonInterceptor(boolean isCommon) {
        this.isCommon = isCommon;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        if (isCommon) {

        }
        return chain.proceed(request.build());
    }




}
