package presenters;

import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bases.MyApplication;
import beans.SuggestBeans;
import beans.ZipBeans;
import inerfaces.ISearchCallback;
import inerfaces.ISearchPresent;
import utils.LogUtil;
import views.SearchUtil;

public class SearchPresent implements ISearchPresent {

    private static final String TAG = "SearchPresent";
    private List<ISearchCallback> mCallbackList=new ArrayList<>();
    private List<ZipBeans> mCurrentZipBeansList =new ArrayList<>();
    private List<SuggestBeans> mSuggestList=new ArrayList<>();
    private List<SuggestBeans> mNewLists=new ArrayList<>();

    private List<SuggestBeans> mHeardWordsList=new ArrayList<>();

    private SearchPresent() {
    }

    private volatile static SearchPresent sPresent;

    public static SearchPresent getPresent() {
        if (sPresent==null){
            synchronized (SearchPresent.class){
                if (sPresent==null){
                    sPresent=new SearchPresent();
                }
            }
        }
        return sPresent;
    }

    @Override
    public void doSearch(String keyWord) {
        //获取全部词库的内容 进行匹配
        mHeardWordsList.clear();
        for (ZipBeans beans : mCurrentZipBeansList) {
            ZipBeans.ContentBeanX.WordBean word = beans.getContent().getWord();
            String tranCn = word.getContent().getTrans().get(0).getTranCn();
            String descCn = word.getContent().getTrans().get(0).getDescCn();
            String ukphone = word.getContent().getUkphone();
            String wordHead = word.getWordHead();
            LogUtil.d(TAG,"wordHead --> "+wordHead);
           // if (wordHead.indexOf(keyWord)>=0) {
            if (SearchUtil.bf(wordHead,keyWord)>0){
                //给搜索结果的数据
               // mHeardWordsList.add(wordHead);
                mHeardWordsList.add(new SuggestBeans(tranCn,descCn,ukphone,wordHead));
            }else{
                for (ISearchCallback iSearchCallback : mCallbackList) {
                    iSearchCallback.onEmpty();
                }
            }
        }
        LogUtil.d(TAG,"有匹配的数据 大小 --> "+mHeardWordsList.size());
        for (ISearchCallback iSearchCallback : mCallbackList) {
            iSearchCallback.onSearchResult(mHeardWordsList);
        }
    }

    @Override
    public void doSuggestWords(String suggestWord) {
        LogUtil.d(TAG,"suggeset size --? "+suggestWord.length());
        mSuggestList.clear();

        for (int i = 0; i < mCurrentZipBeansList.size(); i++) {
            ZipBeans.ContentBeanX.WordBean word = mCurrentZipBeansList.get(i).getContent().getWord();
            String wordHead = word.getWordHead();
            String ukphone = word.getContent().getUkphone();
            String tranCn = word.getContent().getTrans().get(0).getTranCn();
            String descCn = word.getContent().getTrans().get(0).getDescCn();
            int index = wordHead.indexOf(suggestWord);
            if (index >=0){
                mSuggestList.add(new SuggestBeans(tranCn,descCn,ukphone,wordHead));
            }
            LogUtil.d(TAG,"index of --> "+index);
        }
        LogUtil.d(TAG,"suggeList size --> "+mSuggestList.size());
        //因为没匹配到 ——-> 越界报错  2  --> 8
        if (mSuggestList.size()>0 && mSuggestList!=null){
            mNewLists = mSuggestList.subList(0, mSuggestList.size()>6?6:mSuggestList.size());
            LogUtil.d(TAG,"new List size --> "+ mNewLists.size());
            for (ISearchCallback iSearchCallback : mCallbackList) {
                iSearchCallback.onSuggestResult(mNewLists);
            }
        }
    }

    @Override
    public void refreshSearch() {

    }

    @Override
    public void regViewCallback(ISearchCallback callback) {
        if (mCallbackList != null && !mCallbackList.contains(callback)) {
            mCallbackList.add(callback);
        }
    }

    @Override
    public void unRegViewCallback(ISearchCallback callback) {
        if (mCallbackList != null) {
            mCallbackList.remove(callback);
        }
    }

    @Override
    public void getZipList(List<ZipBeans> zipBeansList) {
        if (zipBeansList != null) {
            this.mCurrentZipBeansList =zipBeansList;
        }
    }
}
