package com.wuxiao.rxhttp;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by masai on 2016/10/11.
 */

public interface Api {

    @GET("category/Android/count/10/page/{page}")
    Observable<CategoryData> getCategoryData(@Path("page") int page);

}
