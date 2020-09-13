package presenters;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import beans.SelectBookBeans;
import inerfaces.Api;
import inerfaces.ISelectBookCallback;
import inerfaces.ISelectBookPresent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.LogUtil;

public class SelectBookPresent implements ISelectBookPresent {

    public static final String BASE_URL="https://reciteword.youdao.com/";
    private static final String TAG = "SelectBookPresent";
    private List<ISelectBookCallback> mCallbackList =new ArrayList<>();
    private List<SelectBookBeans.CatesBean> mCurrentCatesBea=new ArrayList<>();

    private SelectBookPresent() {
    }

    private static  volatile SelectBookPresent instance=null;

    public static SelectBookPresent getInstance() {
        if (instance==null){
            synchronized (SelectBookPresent.class){
                if (instance==null){
                    instance=new SelectBookPresent();
                }
            }
        }
        return instance;
    }

    @Override
    public void getBook() {

        //在获取数据之前都要显示加载中
        handlerLoading();

        getPartEnglish();
    }

    private void getPartEnglish() {
        Retrofit mannager = RetrofitMannager.getRetrofitMannager(BASE_URL);
        Api api = mannager.create(Api.class);
        Call<SelectBookBeans> task = api.getSelectJson();
        task.enqueue(new Callback<SelectBookBeans>() {
            @Override
            public void onResponse(Call<SelectBookBeans> call, Response<SelectBookBeans> response) {
                int code = response.code();
                LogUtil.d(TAG,"code --> "+code);

                if (code == HttpURLConnection.HTTP_OK){
                    try {
                        List<SelectBookBeans.CatesBean> cates = response.body().getCates();
                        if (cates != null) {
                            mCurrentCatesBea.addAll(cates);
                                for (ISelectBookCallback iSelectBookCallback : mCallbackList) {
                                    iSelectBookCallback.getBookList(cates,0);
                                }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectBookBeans> call, Throwable t) {
                for (ISelectBookCallback iSelectBookCallback : mCallbackList) {
                    iSelectBookCallback.onNetworkError();
                }
                LogUtil.d(TAG,"error msg --> "+t.toString());
            }
        });
    }

    @Override
    public void getPartBook(int random) {
        //获取catesBeas 然后随机获取里面的内容

    }

    //加载正在加载中的布局
    private void handlerLoading(){
        for (ISelectBookCallback iSelectBookCallback : mCallbackList) {
            iSelectBookCallback.onLoading();
        }
    }

    @Override
    public void regestierSelectBookCallback(ISelectBookCallback callback) {
        if (mCallbackList!=null && !mCallbackList.contains(callback)){
            mCallbackList.add(callback);
        }
    }

    @Override
    public void unRegestierSelectBookCallback(ISelectBookCallback callback) {
        if (mCallbackList != null) {
            mCallbackList.remove(callback);
        }
    }
}
