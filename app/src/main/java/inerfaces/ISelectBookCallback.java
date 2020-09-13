package inerfaces;

import java.util.List;

import beans.SelectBookBeans;

public interface ISelectBookCallback {

    //获取书籍的数据
    void getBookList(List<SelectBookBeans.CatesBean> CatesBeanList,int random);


    //加载中
    void onLoading();

    //网络错误
    void onNetworkError();
}
