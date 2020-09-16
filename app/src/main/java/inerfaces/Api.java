package inerfaces;

import beans.SelectBookBeans;
import okhttp3.ResponseBody;
import presenters.HomePresent;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/*
   网络请求API
 */
public interface Api {

    //这是选择词书的API
    @GET("reciteword/v1/books?le=en&sv=1.1")
    Call<SelectBookBeans> getSelectJson();

    //Home页面点击刷新一个单词的API
    @GET(HomePresent.BASE_URL+"{a}")
    Call<ResponseBody> getSingleJson(@Path("a")String parms);

}
