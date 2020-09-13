package presenters;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMannager {
    private RetrofitMannager() {
    }

    private static volatile Retrofit sRetrofitMannager=null;

    public static Retrofit getRetrofitMannager(String url) {
        if (sRetrofitMannager==null){
            synchronized (RetrofitMannager.class){
                if (sRetrofitMannager==null){
                    sRetrofitMannager=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
                }
            }
        }
        return sRetrofitMannager;
    }
}
