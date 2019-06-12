package tw.com.lixin.wmphonebet.Tools;

import android.util.Log;
import android.widget.TextView;

import tw.com.atromoby.utils.CountDown;
import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.Popup;
import tw.com.lixin.wmphonebet.App;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.global.Poker;
import tw.com.lixin.wmphonebet.global.Url;
import tw.com.lixin.wmphonebet.jsonData.BacData;
import tw.com.lixin.wmphonebet.jsonData.TableData;
import tw.com.lixin.wmphonebet.models.Table;

public class BacSocket extends CasinoSocket {

    private BacBridge bridge;


    public boolean comission = false;
    public boolean cardIsOpening = true;
    public boolean isBettingNow = true;
    public int groupID = -1;
    public int gameID = 109;
    public int areaID;
    public CountDown countDownTimer;
    public CoinStackBack leftBack, rightBack, topBack, lowRightbBack, lowLeftBack, superBack;
    public TableData tableData;
    public int[] pokers;
    public int cardStatus = 0;
    public boolean displayCard = false;

    public String tableRightScore;
    public String tableLeftScore;
    public String tableTopScore;
    public String tableBtlScore;
    public String tableBtrScore;
    public int leftMaxValue;
    public int btRMaxValue;
    public int btLMaxValue;
    public int rightMaxValue;
    public int topMaxValue;
    public int superMaxValue;

    public int pokerWin = -1;

    private Popup winPopup;

    public BacSocket(){
        webUrl = Url.Bac;
        winPopup = new Popup();
    }

    public void bind(BacBridge bridge){
        this.bridge = bridge;
    }

    public void unbind(){
        this.bridge = null;
    }

    @Override
    public void onReceive(String text) {
        BacData bacData = Json.from(text, BacData.class);

        if(bacData.data.gameID != gameID ) return;
        if(bacData.protocol == 26){
            Table ffTable = App.findTable(bacData.data.groupID);
            if(ffTable != null){
                ffTable.setUp(bacData.data.historyArr);
                ffTable.groupType = bacData.data.groupType;
                ffTable.round = bacData.data.historyArr.size();
                ffTable.playCount = bacData.data.historyData.playerCount;
                ffTable.bankCount = bacData.data.historyData.bankerCount;
                ffTable.tieCount = bacData.data.historyData.tieCount;
                ffTable.playPairCount = bacData.data.historyData.playerPairCount;
                ffTable.bankPairCount = bacData.data.historyData.bankerPairCount;
            }else{
                Log.e("ssds", "not catched");
            }
        }
        if(bacData.data.groupID != groupID ) return;

        switch(bacData.protocol) {
            case 10:
                if (bacData.data.bOk) {
                    tableLeftScore = bacData.data.dtOdds.get(2);
                    tableRightScore = bacData.data.dtOdds.get(1);
                    tableBtlScore = bacData.data.dtOdds.get(5);
                    tableBtrScore = bacData.data.dtOdds.get(4);
                    tableTopScore = bacData.data.dtOdds.get(3);
                    leftMaxValue = bacData.data.maxBet02;
                    btLMaxValue = bacData.data.maxBet04;
                    rightMaxValue = bacData.data.maxBet01;
                    btRMaxValue = bacData.data.maxBet04;
                    topMaxValue = bacData.data.maxBet03;
                    superMaxValue = bacData.data.maxBet04;
                    if (bridge != null) handler.post(() -> bridge.groupLogOK());
                } else if (bridge != null) handler.post(() -> bridge.groupLogFail());
                break;
            case 20:
                isBettingNow = false;
                cardIsOpening = false;
                displayCard = false;
                if (bacData.data.gameStage == 1) {
                    pokers = new int[6];
                    isBettingNow = true;
                } else if (bacData.data.gameStage == 2) {
                    cardIsOpening = true;
                    countDownTimer.cancel();
                    displayCard = true;
                }
                cardStatus = bacData.data.gameStage;
                if (bridge != null) handler.post(() -> bridge.cardStatusUpdate());
                break;
            case 22:
                if (bacData.data.bOk) {
                    if(bridge != null) handler.post(() -> bridge.betOK());
                }else {
                    if(bridge != null) handler.post(() -> bridge.betFail());
                }
                break;
            case 23:
                if(bridge != null) handler.post(() -> bridge.balanceUpdate(bacData.data.balance));
                break;
            case 24:
                if (bacData.data.cardArea == 3) {
                    pokers[0] = Poker.NUM(bacData.data.cardID);
                } else if (bacData.data.cardArea == 2) {
                    pokers[3] = Poker.NUM(bacData.data.cardID);
                } else if (bacData.data.cardArea == 4) {
                    pokers[4] = Poker.NUM(bacData.data.cardID);
                } else if (bacData.data.cardArea == 6) {
                    pokers[5] = Poker.NUM(bacData.data.cardID);
                } else if (bacData.data.cardArea == 1) {
                    pokers[1] = Poker.NUM(bacData.data.cardID);
                } else if (bacData.data.cardArea == 5) {
                    pokers[2] = Poker.NUM(bacData.data.cardID);
                }
                if(bridge != null) handler.post(() -> bridge.cardAreaUpadte());
                break;
            case 25:
                pokerWin = Move.divide(bacData.data.result);

                break;
            case 26:

                break;
            case 31:

                int leftVal = leftBack.



                TextView mText = winPopup.findViewById(R.id.player_bet);
                mText.setText(stackLeft.value + "");
                mText = winPopup.findViewById(R.id.banker_bet);
                mText.setText(stackRight.value + "");
                mText = winPopup.findViewById(R.id.player_pair_bet);
                mText.setText(stackBTL.value + "");
                mText = winPopup.findViewById(R.id.banker_pair_bet);
                mText.setText(stackBTR.value + "");
                mText = winPopup.findViewById(R.id.tie_bet);
                mText.setText(stackTop.value + "");
                mText = winPopup.findViewById(R.id.super_bet);
                mText.setText(stackSuper.value + "");

                mText = winPopup.findViewById(R.id.player_win);
                if (data.dtMoneyWin.get(2) == null) {
                    mText.setText("");
                } else {
                    mText.setText(data.dtMoneyWin.get(2) + "");
                }
                mText = winPopup.findViewById(R.id.banker_win);
                if (data.dtMoneyWin.get(1) == null) {
                    mText.setText("");
                } else {
                    mText.setText(data.dtMoneyWin.get(1) + "");
                }
                mText = winPopup.findViewById(R.id.player_pair_win);
                if (data.dtMoneyWin.get(5) == null) {
                    mText.setText("");
                } else {
                    mText.setText(data.dtMoneyWin.get(5) + "");
                }
                mText = winPopup.findViewById(R.id.banker_pair_win);
                if (data.dtMoneyWin.get(4) == null) {
                    mText.setText("");
                } else {
                    mText.setText(data.dtMoneyWin.get(4) + "");
                }
                mText = winPopup.findViewById(R.id.tie_win);
                if (data.dtMoneyWin.get(3) == null) {
                    mText.setText("");
                } else {
                    mText.setText(data.dtMoneyWin.get(3) + "");
                }
                mText = winPopup.findViewById(R.id.super_win);
                if (data.dtMoneyWin.get(8) == null) {
                    mText.setText("");
                } else {
                    mText.setText(data.dtMoneyWin.get(8) + "");
                }

                mText = winPopup.findViewById(R.id.total_win_money);
                mText.setText(data.moneyWin + "");
                winPopup.show();


                break;
            case 38:
                handler.post(() -> countDownTimer.start(bacData.data.timeMillisecond, i->{
                    if(!cardIsOpening){
                        if(bridge != null) bridge.betCountdown(i);
                    }
                }));
                break;
        }
    }
}
