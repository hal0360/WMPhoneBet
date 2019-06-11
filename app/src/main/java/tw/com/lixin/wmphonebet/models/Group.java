package tw.com.lixin.wmphonebet.models;

import android.os.Handler;
import android.util.Log;

import tw.com.atromoby.utils.CountDown;
import tw.com.lixin.wmphonebet.Tools.CoinStackBack;
import tw.com.lixin.wmphonebet.global.Poker;
import tw.com.lixin.wmphonebet.jsonData.Server10;
import tw.com.lixin.wmphonebet.jsonData.Server20;
import tw.com.lixin.wmphonebet.jsonData.Server22;
import tw.com.lixin.wmphonebet.jsonData.Server24;
import tw.com.lixin.wmphonebet.jsonData.Server25;
import tw.com.lixin.wmphonebet.jsonData.Server26;
import tw.com.lixin.wmphonebet.jsonData.Server31;
import tw.com.lixin.wmphonebet.jsonData.Server38;
import tw.com.lixin.wmphonebet.jsonData.data.Server23;

public class Group {

    public boolean comission = false;

    public CasinoGroupBridge bridge;
    public boolean cardIsOpening = true;
    public boolean isBettingNow = true;
    public int groupID = -1;
    public int areaID;
    public CountDown countDownTimer;

    public CoinStackBack leftBack, rightBack, topBack, lowRightbBack, lowLeftBack, superBack;

    public Server10.Data data10;
    public int[] pokers;

    public int cardStatus = 0;

    public boolean displayCard = false;

    private Handler handler;

  //  private SparseArray<int> pokers = new SparseArray<>();

    public Group(){
        countDownTimer = new CountDown();
        leftBack = new CoinStackBack();
        rightBack = new CoinStackBack();
        topBack = new CoinStackBack();
        lowLeftBack = new CoinStackBack();
        lowRightbBack = new CoinStackBack();
        superBack = new CoinStackBack();
        pokers = new int[6];
        handler = new Handler();
    }

    public void setUp(CasinoGroupBridge bridge){
        this.bridge = bridge;
    }

    public void pro20(Server20.Data data){

        Log.e("group", "20 catched");


        isBettingNow = false;
        cardIsOpening = false;
        displayCard = false;

        if (data.gameStage == 1) {
            pokers = new int[6];
            isBettingNow = true;
        } else if (data.gameStage == 2) {
            cardIsOpening = true;
            countDownTimer.cancel();
            displayCard = true;
        }

        cardStatus = data.gameStage;

        if(bridge != null) handler.post(() -> bridge.CardStatus());
    }

    public void pro24(Server24.Data data){
        if (data.cardArea == 3) {
            pokers[0] = Poker.NUM(data.cardID);
        } else if (data.cardArea == 2) {
            pokers[3] = Poker.NUM(data.cardID);
        } else if (data.cardArea == 4) {
            pokers[4] = Poker.NUM(data.cardID);
        } else if (data.cardArea == 6) {
            pokers[5] = Poker.NUM(data.cardID);
        } else if (data.cardArea == 1) {
            pokers[1] = Poker.NUM(data.cardID);
        } else if (data.cardArea == 5) {
            pokers[2] = Poker.NUM(data.cardID);
        }
        if(bridge != null) handler.post(() -> bridge.cardArea(data));
    }


    public void pro26(Server26.Data data){
        if(bridge != null) handler.post(() -> bridge.gridUpdate(data));
    }

    public void pro25(Server25.Data data){
        if(bridge != null) handler.post(() -> bridge.winLossResult(data));
    }

    public void pro31(Server31.Data data){
        if(bridge != null) handler.post(() -> bridge.moneWon(data));
    }

    public void pro38(Server38.Data data){

        handler.post(() -> {
            countDownTimer.start(data.timeMillisecond, i->{
                if(!cardIsOpening){
                      if(bridge != null) bridge.betCountdown(i);
                }
            });
        });


    }

    public void pro22(Server22.Data data){
        if (data.bOk) {
            if(bridge != null) handler.post(() -> bridge.betOK());
        }
    }

    public void pro23(Server23.Data data){
        if(bridge != null) handler.post(() -> bridge.balance(data.balance));
    }

}
