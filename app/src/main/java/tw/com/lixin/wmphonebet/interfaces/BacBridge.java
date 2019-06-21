package tw.com.lixin.wmphonebet.interfaces;

public interface BacBridge {
    void cardStatusUpdate();
    void cardAreaUpadte();
    void balanceUpdate(float value);
    void betOK();
    void betFail();
    void gridUpdate();
    void betCountdown(int sec);
    void groupLogOK();
    void groupLogFail();

}
