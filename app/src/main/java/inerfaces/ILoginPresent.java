package inerfaces;

public interface ILoginPresent {

    void doLogin();

    void regLoginCallback(ILoginCallback callback);

    void unReLoginCallback(ILoginCallback callback);
}
