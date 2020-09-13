package inerfaces;

import beans.SelectBookBeans;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/*
   网络请求API
 */
public interface Api {

    @GET("reciteword/v1/books?le=en&sv=1.1")
    Call<SelectBookBeans> getSelectJson();
}
