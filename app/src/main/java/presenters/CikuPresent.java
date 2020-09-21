package presenters;

import java.util.ArrayList;
import java.util.List;

import beans.ZipBeans;
import inerfaces.ICiKuPresent;
import inerfaces.ICikuCallback;
import utils.ListPageUtils;
import utils.LogUtil;

public class CikuPresent implements ICiKuPresent {

    private static final String TAG = "CikuPresent";
    private List<ZipBeans> mCurrentList =new ArrayList<>();
    private List<ICikuCallback> mCallbackList=new ArrayList<>();
    private int mPageCount=1;
    private ListPageUtils<ZipBeans> mPageUtils;


    private CikuPresent() {
    }

    private static volatile  CikuPresent sPresent;

    public static CikuPresent getPresent() {
        if (sPresent==null){
            synchronized (CikuPresent.class){
                if (sPresent==null){
                    sPresent=new CikuPresent();
                }
            }
        }
        return sPresent;
    }

    @Override
    public void requestAllWords() {
        for (ICikuCallback iCikuCallback : mCallbackList) {
            iCikuCallback.showAllWords(mPageUtils.getPagedList(mPageCount));

        }
    }

    @Override
    public void regesiterCikuCallback(ICikuCallback callback) {
        if (!mCallbackList.contains(callback) && mCallbackList!=null) {
            mCallbackList.add(callback);
        }
    }

    @Override
    public void unRegesiterCikuCallback(ICikuCallback callback) {
        if (mCallbackList != null) {
            mCallbackList.remove(callback);
        }
    }

    @Override
    public void loaderMore() {
        mPageCount++;
        doLoader(true);
    }

    private void doLoader(boolean isLoader) {
        if (isLoader){
           mCurrentList.clear();
            //开始加载数据
            List<ZipBeans> pagedList = mPageUtils.getPagedList(mPageCount);
            mCurrentList.addAll(pagedList);
            for (ICikuCallback iCikuCallback : mCallbackList) {
                iCikuCallback.showAllWords(mCurrentList);
                iCikuCallback.LoaderMore(pagedList.size());
            }
        }
    }

    //赋值的时候 顺便把BeanList直接拆分成20一组
    public void setWordkList(List<ZipBeans> beansList) {
        if (beansList != null) {
            //分页20条数据
            mPageUtils = new ListPageUtils<>(beansList,20);
        }
    }
}
