package inerfaces;

public interface ICiKuPresent {
    //获取 全部单词
    void requestAllWords();

    //注册回调
    void regesiterCikuCallback(ICikuCallback callback);

    void unRegesiterCikuCallback(ICikuCallback callback);

    //上啦加载
    void loaderMore();
}
