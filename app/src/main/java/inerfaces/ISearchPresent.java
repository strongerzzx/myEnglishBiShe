package inerfaces;

import java.util.List;

import beans.ZipBeans;

public interface ISearchPresent {

    //开始搜索
    void  doSearch(String keyWord);

    //获取联想词
    void doSuggestWords(String suggestWord);

    //重新搜索
    void refreshSearch();

    void regViewCallback(ISearchCallback callback);

    void unRegViewCallback(ISearchCallback callback);

    void getZipList(List<ZipBeans> currentZipList);
}
