package tw.com.lixin.wmphonebet.Tools;

import android.util.Log;

import tw.com.atromoby.utils.CountDown;
import tw.com.atromoby.utils.Json;
import tw.com.lixin.wmphonebet.App;
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




    public BacSocket(){
        webUrl = Url.Bac;
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
                int pokerWin = Move.divide(bacData.data.result);
                if (pokerWin == 1) {
                    pokerBall.setText(getString(R.string.banker_score));
                    pokerBall.setBackgroundResource(R.drawable.casino_item_bt_bank);
                } else if (pokerWin == 2) {
                    pokerBall.setText(getString(R.string.player_score));
                    pokerBall.setBackgroundResource(R.drawable.casino_item_bt_player);
                } else {
                    pokerBall.setText(getString(R.string.tie_score));
                    pokerBall.setBackgroundResource(R.drawable.casino_item_bt_bank);
                }
                playerScreenScore.setText(getString(R.string.player_score) + data.playerScore);
                bankerScreenScore.setText(getString(R.string.banker_score) + data.bankerScore);
                pokerBall.setVisibility(View.VISIBLE);
                break;
        }
    }
}
