package presenters;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import bases.MyApplication;
import beans.SelectBookBeans;
import beans.ZipBeans;
import inerfaces.Api;
import inerfaces.IHomeCallback;
import inerfaces.IHomePresent;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.LogUtil;

public class HomePresent implements IHomePresent {

    private static final String TAG = "HomePresent";
    private List<IHomeCallback> mList=new ArrayList<>();
    private int mCurrentPosition=0;
    private String mCurrentUrl;

    public static final String BASE_URL="http://ydschool-online.nos.netease.com/";
    private String mQueryParms;
    private String mDownName;


    private HomePresent() {
    }

    private volatile static HomePresent sPresent;

    public static HomePresent getPresent() {
        if (sPresent==null){
            synchronized (HomePresent.class){
                if (sPresent==null){
                    sPresent=new HomePresent();
                }
            }
        }
        return sPresent;
    }

    @Override
    public void regesiterHomeCallback(IHomeCallback callback) {
        if (!mList.contains(callback) && mList != null) {
            mList.add(callback);
        }
    }

    @Override
    public void unRegesiterHomeCallback(IHomeCallback callback) {
        if (mList != null) {
            mList.remove(callback);
        }
    }


    //根据点击哪一个书本 --> 来显示要显示的单词
    public void setSingleZip(List<SelectBookBeans.CatesBean.BookListBean> mSelectBean, int position){
        if (mSelectBean != null) {
            String offlinedata = mSelectBean.get(position).getOfflinedata();
            this.mCurrentUrl=offlinedata;
            LogUtil.d(TAG,"mCurr --> "+mCurrentUrl);

            //查询参数
            mQueryParms = mCurrentUrl.replace(BASE_URL, "");

            //获取下载后的zip名字
            int i = mQueryParms.indexOf("_");
            if (mQueryParms.length()>0 && mQueryParms!=null){
                StringBuffer sb=new StringBuffer();
                sb.append(mQueryParms);
                mDownName = sb.replace(0, i + 1, "").toString();
            }
        }
        this.mCurrentPosition=position;
    }

    @Override
    public void getSingleWords() {

        //下载服务器上的zip包
        Retrofit mannager = RetrofitMannager.getRetrofitMannager("");
        Api api = mannager.create(Api.class);
        Call<ResponseBody> task = api.getSingleJson(mQueryParms);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                LogUtil.d(TAG,"code --> "+ code);

                if (code==HttpURLConnection.HTTP_OK){
                    writeZip2SDCard(response.body());

                    readDataInZip(new File(mDownName));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtil.d(TAG,"error --> "+t.toString());
            }
        });


    }

    private void readDataInZip(File downName) {
        FileInputStream fis=null;
        int len=0;
        byte[] bytes=new byte[1024];
        try {
            fis=new FileInputStream(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+downName);
            while ((len=fis.read(bytes))!=-1){
                //TODO:读取本地的zip内容
                String json=new String(bytes,0,bytes.length,"UTF-8");
                Gson gson=new Gson();
                ZipBeans zipBeans = gson.fromJson(json, ZipBeans.class);
                LogUtil.d(TAG,"zipb --> "+zipBeans.getHeadWord());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeZip2SDCard(ResponseBody body) {

        File exterDir = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        LogUtil.d(TAG,"down --> "+mDownName);
        File totalFile=new File(exterDir,mDownName);
        byte[] bytes=new byte[1024];
        int len=0;
        FileOutputStream fos=null;
        InputStream is = null;
        if (!totalFile.exists()){
            try {
                totalFile.createNewFile();
                is=body.byteStream();
                fos=new FileOutputStream(totalFile);
                while ((len=is.read(bytes))!=-1){
                    fos.write(bytes,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (fos!=null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


}
