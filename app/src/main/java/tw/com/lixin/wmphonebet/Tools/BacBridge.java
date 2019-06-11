package tw.com.lixin.wmphonebet.Tools;

import tw.com.lixin.wmphonebet.jsonData.Server24;
import tw.com.lixin.wmphonebet.jsonData.Server25;
import tw.com.lixin.wmphonebet.jsonData.Server26;
import tw.com.lixin.wmphonebet.jsonData.Server31;

public interface BacBridge {
    void cardStatusUpdate();
    void cardAreaUpadte();
    void balanceUpdate(float value);
    void betOK();
    void betFail();
    void gridUpdate();
    void winLossResult(Server25.Data data);
    void moneWon(Server31.Data data);
    void betCountdown(int sec);
    void groupLogOK();
    void groupLogFail();

}
