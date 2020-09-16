package inerfaces;

public interface IHomePresent {
    //注册回调
    void regesiterHomeCallback(IHomeCallback callback);

    void unRegesiterHomeCallback(IHomeCallback callback);


    //获取单个单词
    void getSingleWords();

}
