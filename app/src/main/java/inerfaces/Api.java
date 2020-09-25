package inerfaces;

import com.example.englishapp.RegActivity;

import java.util.Map;

import beans.CollectionBeans;
import beans.LoginBeans;
import beans.RegBeans;
import beans.SelectBookBeans;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import presenters.HomePresent;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/*
   网络请求API
 */
public interface Api {

    //这是选择词书的API
    @Headers("urlname:text1")
    @GET("reciteword/v1/books?le=en&sv=1.1")
    Call<SelectBookBeans> getSelectJson();

    //Home页面点击刷新一个单词的API
    @Headers("urlname:text1")
    @GET(HomePresent.BASE_URL+"{a}")
    Call<ResponseBody> getSingleJson(@Path("a")String parms);


    //注册账号
    @Headers({"Content-Type:application/json;charset=UTF-8","User-Agent:Retrofit-your-App","urlname:text2"})
    @POST("register")
    Call<RegBeans> regAccount(@Body RequestBody requestBody);


    //登陆
    @Headers({"Content-Type:application/json;charset=UTF-8","User-Agent:Retrofit-your-App","urlname:text2"})
    @POST("login")
    Call<LoginBeans> doLogin(@Body RequestBody requestBody);


    //收藏
    @Headers({"Content-Type:application/json;charset=UTF-8","User-Agent:Retrofit-your-App","urlname:text2"})
    @POST("favorites")
    Call<CollectionBeans> doCollection(@Body RequestBody requestBody);


}
