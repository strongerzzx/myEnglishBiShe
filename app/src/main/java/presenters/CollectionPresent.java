package presenters;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import beans.CollectionBeans;
import beans.ContentUrl;
import beans.ZipBeans;
import inerfaces.Api;
import inerfaces.ICollectionPresent;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.LogUtil;

public class CollectionPresent implements ICollectionPresent {

    private static final String TAG = "CollectionPresent";
    private int collId;
    private String collTran1;
    private String collTran;
    private String collHeadWord;
    private int collCommit;

    private CollectionPresent() {
    }

    private static CollectionPresent sPresent;

    public static CollectionPresent getPresent() {
        if (sPresent==null){
            synchronized (CollectionPresent.class){
                if (sPresent==null){
                    sPresent=new CollectionPresent();
                }
            }
        }
        return sPresent;
    }


    //收藏
    @Override
    public void doCollection() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ContentUrl.BASE_LOGIN).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Map<String,Object> map=new HashMap<>();
        HashMap<Object, Object> map2 = new HashMap<>();
        map.put("userId",collCommit);
        map2.put("id", collId);
        map2.put("headWord", collHeadWord);
        map2.put("tranCn", collTran);
        map2.put("descCn", collTran1);
        map.put("wordBean",map2);
        String s = new Gson().toJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), s);
        Call<CollectionBeans> call = api.doCollection(body);
        call.enqueue(new Callback<CollectionBeans>() {
            @Override
            public void onResponse(Call<CollectionBeans> call, Response<CollectionBeans> response) {
                int code = response.code();
                LogUtil.d(TAG,"CODE --> "+code);
                if (code== HttpURLConnection.HTTP_OK){
                    String msg = response.body().getMsg();
                    boolean flag = response.body().isFlag();

                    LogUtil.d(TAG,"flag --> "+flag);
                    LogUtil.d(TAG,"msg -->"+msg);
                }
            }
            @Override
            public void onFailure(Call<CollectionBeans> call, Throwable t) {
                LogUtil.d(TAG,"error --> "+t.toString());
            }
        });
    }

    public void setData(int position, String tranCn, String tranCn1, String headWord) {
        this.collId=position;
        this.collTran=tranCn;
        this.collTran1=tranCn1;
        this.collHeadWord=headWord;
    }

    public void commitID(int id) {
        this.collCommit=id;
    }
}
