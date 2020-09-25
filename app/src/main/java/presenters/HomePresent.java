package presenters;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import bases.MyApplication;
import beans.SelectBookBeans;
import beans.ZipBeans;
import inerfaces.Api;
import inerfaces.IHomeCallback;
import inerfaces.IHomePresent;
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
    private boolean isRandom=true;

    public static final String BASE_URL="http://ydschool-online.nos.netease.com/";
    private String mQueryParms;
    private String mDownName;
    private List<ZipBeans> mCurrentZipList=new ArrayList<ZipBeans>();


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
            isRandom=true;
        }
    }

    @Override
    public void unRegesiterHomeCallback(IHomeCallback callback) {
        if (mList != null) {
            isRandom=false;
            mList.remove(callback);
        }
    }


    //根据点击哪一个书本 --> 来显示要显示的单词
    public void setSingleZip(List<SelectBookBeans.CatesBean.BookListBean> mSelectBean, int position){
        if (mSelectBean != null) {
            String offlinedata = mSelectBean.get(position).getOfflinedata();
            this.mCurrentUrl=offlinedata;

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

        requestZipLoading();
        //下载服务器上的zip包
        isRandom=true;
        Retrofit mannager = RetrofitMannager.getRetrofitMannager("");
        Api api = mannager.create(Api.class);
        Call<ResponseBody> task = api.getSingleJson(mQueryParms);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                LogUtil.d(TAG,"code --> "+ code);
                if (code==HttpURLConnection.HTTP_OK){
                    File zipFile = writeZip2SDCard(response.body());
                    //读取zip包内容
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                          //  jieyaZipLoading();
                            readDataInZip(zipFile);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtil.d(TAG,"error --> "+t.toString());
            }
        });


    }

    private void requestZipLoading(){
        for (IHomeCallback iHomeCallback : mList) {
            iHomeCallback.onRequestLoading();
        }
    }


    private void readDataInZip(File file) {


        BufferedReader br = null;
        ZipInputStream zis = null;
        try {
            ZipFile zipFile = new ZipFile(file);
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            zis = new ZipInputStream(is);
            ZipEntry entry;
            StringBuffer sb = new StringBuffer();
            //存储bean里的单词  因为给的json是错的 必须要重新组装 里面没有List
            List<ZipBeans> beansList = new ArrayList<>();
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    LogUtil.d(TAG, "是dir");
                } else {
                    br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
                    String line = "";
                    sb.append("[");
                    while ((line = br.readLine()) != null) {
                        Gson gson = new Gson();
                        ZipBeans beans = gson.fromJson(line, ZipBeans.class);
                        beansList.add(beans);
                    }
                    sb.append("]");

                    mCurrentZipList.addAll(beansList);
                    //数据传给Seacrch的Present
                    SearchPresent.getPresent().getZipList(mCurrentZipList);
                    LogUtil.d(TAG,"currentList --> "+mCurrentZipList.size());
                }
            }
            CikuPresent.getPresent().setWordkList(beansList);

            //结束加载
            finshLoading();

            while (isRandom){
                Random random=new Random();
                int randomIndex = random.nextInt(mCurrentZipList.size());
                String tranCn = mCurrentZipList.get(randomIndex).getContent().getWord().getContent().getTrans().get(0).getTranCn();
                String wordHead = mCurrentZipList.get(randomIndex).getContent().getWord().getWordHead();
                for (IHomeCallback iHomeCallback : mList) {
                    iHomeCallback.showSingleWords(wordHead,tranCn);
                    isRandom=true;
                }
                if (!isRandom){
                    break;
                }
            }
            LogUtil.d(TAG,"list size --> "+mCurrentZipList.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void finshLoading() {
        for (IHomeCallback iHomeCallback : mList) {
            iHomeCallback.onFinishLoading();
        }
    }

    private File writeZip2SDCard(ResponseBody body) {

        File exterDir = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        LogUtil.d(TAG,"down --> "+mDownName);
        File totalFile=new File(exterDir,mDownName);
        LogUtil.d(TAG,"totalfile ---> "+totalFile.getPath());
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
        return totalFile;
    }


}
