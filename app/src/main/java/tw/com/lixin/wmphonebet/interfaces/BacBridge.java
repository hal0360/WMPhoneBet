package tw.com.lixin.wmphonebet.interfaces;

import tw.com.lixin.wmphonebet.jsonData.BacData;

public interface BacBridge {
    void statusUpdate();
    void cardUpdate(int area, int img);
    void resultUpadte();
    void balanceUpdate(float value);
    void betUpdate(boolean betOK);
    void gridUpdate();
    void betCountdown(int sec);
    void winLossUpdate(BacData data);
}
