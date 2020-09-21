package inerfaces;

public interface IHomeCallback {
    //显示单个单词
    void showSingleWords(String singleEnglish,String singleChinese);

    //服务器请求中zip
    void onRequestLoading();


    //数据加载完成
    void onFinishLoading();

}
