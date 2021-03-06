package presenters;

import android.content.Intent;
import android.util.Log;

import com.example.englishapp.LoginActivity;
import com.example.englishapp.RegActivity;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import beans.RegBeans;
import inerfaces.Api;
import inerfaces.IRegCallback;
import inerfaces.IRegPresent;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.LogUtil;

public class RegPresent implements IRegPresent {

    private static final String BASU_REG_URL ="http://47.100.170.185:8081/";
    private static final String TAG = "RegPresent";
    private List<IRegCallback> mCallbacks=new ArrayList<>();
    private String mCurrentPswd;
    private String mCurrentAccount;

    private RegPresent() {
    }

    private static RegPresent sPresent;

    public static RegPresent getPresent() {
        if (sPresent==null){
            synchronized (RegPresent.class){
                if (sPresent==null){
                    sPresent=new RegPresent();
                }
            }
        }
        return sPresent;
    }

    @Override
    public void requestReg() {
        Retrofit retrofit = RetrofitMannager.getRetrofitMannager(BASU_REG_URL);
        Api api = retrofit.create(Api.class);

        Map<String, String> map = new HashMap<>();
        map.put("username", mCurrentAccount.trim());
        map.put("password", mCurrentPswd.trim());
        String s = new Gson().toJson(map);
        Log.d(TAG, "onClick: "+s);
        //post 提交必须要有这个body
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), s);
        Call<RegBeans> task = api.regAccount(requestBody);
        task.enqueue(new Callback<RegBeans>() {
            @Override
            public void onResponse(Call<RegBeans> call, Response<RegBeans> response) {
                int code = response.code();
                LogUtil.d(TAG,"code --> "+code);
                if (code== HttpURLConnection.HTTP_OK){
                    String msg = response.body().getMsg();
                    boolean flag = response.body().isFlag();
                    LogUtil.d(TAG,":"+msg+":"+flag);
                    LogUtil.d(TAG,"body --> "+response.body());
                }
            }

            @Override
            public void onFailure(Call<RegBeans> call, Throwable t) {
                LogUtil.d(TAG,"error --> "+t.toString());
            }
        });
    }

    @Override
    public void regRegCallback(IRegCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegRegCallback(IRegCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }



    public void getAccount(String account) {
        this.mCurrentAccount=account;

    }

    public void getPswd(String pswd) {
        this.mCurrentPswd=pswd;
    }
}
