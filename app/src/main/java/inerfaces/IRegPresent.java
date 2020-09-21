package inerfaces;

public interface IRegPresent {
    void requestReg();

    void regRegCallback(IRegCallback callback);

    void unRegRegCallback(IRegCallback callback);

}
