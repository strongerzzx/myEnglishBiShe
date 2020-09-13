package inerfaces;

public interface ISelectBookPresent {

    //获取书籍的数据
    void getBook();

    void getPartBook(int random);

    //注册回调接口
    void regestierSelectBookCallback(ISelectBookCallback callback);

    //取消回调接口
    void unRegestierSelectBookCallback(ISelectBookCallback callback);
}
