package inerfaces;

import java.util.List;

import beans.ZipBeans;

public interface ICikuCallback {
    //显示全部单词
    void showAllWords(List<ZipBeans> beansList);


    //上啦刷新数据
    void LoaderMore(int size);

}
