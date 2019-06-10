package tw.com.lixin.wmphonebet.Tools;

import tw.com.lixin.wmphonebet.jsonData.Server24;
import tw.com.lixin.wmphonebet.jsonData.Server25;
import tw.com.lixin.wmphonebet.jsonData.Server26;
import tw.com.lixin.wmphonebet.jsonData.Server31;

public interface BacBridge {
    void cardStatusUpdate();
    void cardAreaUpadte(Server24.Data data);
    void balance(float value);
    void betOK();
    void gridUpdate(Server26.Data data);
    void winLossResult(Server25.Data data);
    void moneWon(Server31.Data data);
    void betCountdown(int sec);

}
