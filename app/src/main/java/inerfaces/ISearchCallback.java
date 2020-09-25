package inerfaces;

import java.util.List;

import beans.SuggestBeans;
import beans.ZipBeans;

public interface ISearchCallback {

//    //返回搜索结果
//    void onSearchResult(List<String> mHeardWordsList);

    //返回搜索结果
    void onSearchResult(List<SuggestBeans> mHeardWordsList);


    //返回热词
    void onSuggestResult(List<SuggestBeans> suggestList);

    //空数据
    void onEmpty();

    //加载中
    void onLoading();
}
