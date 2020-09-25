package presenters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import beans.ContentUrl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.BaseUrlInterceptor;
import utils.LogUtil;

public class RetrofitMannager {
    private static final String TAG = "RetrofitMannager";

    private RetrofitMannager() {
    }

    private static  Retrofit sRetrofitMannager=null;

    public static Retrofit getRetrofitMannager(String url) {
        if (sRetrofitMannager==null){
            synchronized (RetrofitMannager.class){
                if (sRetrofitMannager==null){

//                    OkHttpClient client=new OkHttpClient
//                            .Builder()
//                            .addInterceptor(new BaseUrlInterceptor())
//                            .build();

                    OkHttpClient client = new OkHttpClient.Builder()
                            .readTimeout(3000, TimeUnit.MILLISECONDS)
                            .connectTimeout(3000, TimeUnit.MILLISECONDS)
                            //.addInterceptor(mRewriteCacheControlInterceptor)//没网的情况下
                            //.addNetworkInterceptor(mRewriteCacheControlInterceptor)//有网的情况下
                            //.addInterceptor(new BaseUrlInterceptor())
                            //.addInterceptor(logInterceptor)
                            //.cache(cache)
                            .build();

                    sRetrofitMannager=new Retrofit.Builder()
                            .baseUrl(url)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                }
            }
        }
        return sRetrofitMannager;
    }
}
