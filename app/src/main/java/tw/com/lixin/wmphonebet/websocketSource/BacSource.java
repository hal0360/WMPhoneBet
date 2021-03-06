package tw.com.lixin.wmphonebet.websocketSource;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.TextView;

import tw.com.atromoby.utils.Cmd;
import tw.com.atromoby.utils.CountDown;
import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.Popup;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.interfaces.BacBridge;
import tw.com.lixin.wmphonebet.interfaces.CmdBool;
import tw.com.lixin.wmphonebet.models.CoinStackData;
import tw.com.lixin.wmphonebet.Tools.Move;
import tw.com.lixin.wmphonebet.global.Poker;
import tw.com.lixin.wmphonebet.global.Url;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.jsonData.BacData;
import tw.com.lixin.wmphonebet.jsonData.Client10;
import tw.com.lixin.wmphonebet.models.Table;

public class BacSource extends CasinoSource{

    private static BacSource single_instance = null;
    public static BacSource getInstance()
    {
        if (single_instance == null) single_instance = new BacSource();
        return single_instance;
    }
    private BacSource() {
        defineURL("ws://gameserver.a45.me:15101");
        countDownTimer = new CountDown();
    }

    private BacBridge bridge;
    public boolean comission = false;
   // public boolean cardIsOpening = false;
    //public boolean isBettingNow = true;

    public int groupID = -1;
    public int gameID = 101;
    public int areaID;
    public CountDown countDownTimer;
    public CoinStackData stackLeft, stackRight, stackBTL, stackBTR, stackTop, stackSuper;
    public SparseIntArray pokers;
    public int status = 0;
   // public boolean displayCard = false;

    public String tableRightScore;
    public String tableLeftScore;
    public String tableTopScore;
    public String tableBtlScore;
    public String tableBtrScore;

    public int pokerWin = -1;
    public int maxBetVal;
    public int playerScore, bankerScore;
    private CmdBool cOk;
    public Table table;

    public void bind(BacBridge bridge){
        this.bridge = bridge;
    }

    public void unbind(){
        this.bridge = null;
    }

    public void handle(Cmd cmd){
        super.handle(() ->{
            if(bridge != null) cmd.exec();
        });
    }

    public void tableLogin(Table table, CmdBool cmd){
        cOk = cmd;
        this.table = table;
        groupID = table.groupID;
        Client10 client = new Client10(table.groupID);
        send(Json.to(client));
    }

    @Override
    public void onReceive(String text) {
        Log.e("bacSocket", text);
        BacData bacData = Json.from(text, BacData.class);
        if(bacData.data.gameID != gameID || bacData.data.groupID != groupID) return;
        if(bacData.protocol == 10){
            if (bacData.data.bOk) {
                comission = false;
                stackSuper = new CoinStackData();
                stackTop = new CoinStackData();
                stackBTR = new CoinStackData();
                stackRight = new CoinStackData();
                stackBTL = new CoinStackData();
                stackLeft = new CoinStackData();
                pokers = new SparseIntArray();
                tableLeftScore = bacData.data.dtOdds.get(2);
                tableRightScore = bacData.data.dtOdds.get(1);
                tableBtlScore = bacData.data.dtOdds.get(5);
                tableBtrScore = bacData.data.dtOdds.get(4);
                tableTopScore = bacData.data.dtOdds.get(3);
                stackLeft.maxValue = bacData.data.maxBet02;
                stackBTL.maxValue = bacData.data.maxBet04;
                stackRight.maxValue = bacData.data.maxBet01;
                stackBTR.maxValue = bacData.data.maxBet04;
                stackTop.maxValue = bacData.data.maxBet03;
                stackSuper.maxValue = bacData.data.maxBet04;
                maxBetVal = bacData.data.maxBet01;
                if(maxBetVal < bacData.data.maxBet02) maxBetVal = bacData.data.maxBet02;
                if(maxBetVal < bacData.data.maxBet03) maxBetVal = bacData.data.maxBet03;
                if(maxBetVal < bacData.data.maxBet04) maxBetVal = bacData.data.maxBet04;
            }
            if(cOk != null) super.handle(()-> {
                cOk.exec(bacData.data.bOk);
                cOk = null;
            });
        }else if(bacData.protocol == 20){
           // isBettingNow = false;
          //  cardIsOpening = false;
           // displayCard = false;
            if (bacData.data.gameStage == 1) {
                pokers = new SparseIntArray();
               // isBettingNow = true;
                pokerWin = -1;
            } else if (bacData.data.gameStage == 2) {
              //  cardIsOpening = true;
                countDownTimer.cancel();
               // displayCard = true;
            }
            status = bacData.data.gameStage;
            handle(() -> bridge.statusUpdate());
        }else if(bacData.protocol == 22){
            if(bacData.data.bOk){
                stackLeft.comfirmBet();
                stackRight.comfirmBet();
                stackBTL.comfirmBet();
                stackBTR.comfirmBet();
                stackTop.comfirmBet();
                stackSuper.comfirmBet();
            }
            handle(() -> bridge.betUpdate(bacData.data.bOk));
        }else if(bacData.protocol == 23){
            handle(() -> bridge.balanceUpdate(bacData.data.balance));
        }else if(bacData.protocol == 24){
            pokers.put(bacData.data.cardArea,Poker.NUM(bacData.data.cardID));
            handle(() -> bridge.cardUpdate(bacData.data.cardArea, Poker.NUM(bacData.data.cardID)));
        }else if(bacData.protocol == 25){
            pokerWin = Move.divide(bacData.data.result);
            playerScore = bacData.data.playerScore;
            bankerScore = bacData.data.bankerScore;
            handle(() -> bridge.resultUpadte());
        }else if(bacData.protocol == 26){
            table.update(bacData);
            handle(() -> bridge.gridUpdate());
        }else if(bacData.protocol == 31){
            if(User.memberID() != bacData.data.memberID || bridge == null) return;
            handle(() -> bridge.winLossUpdate(bacData));
        }else if(bacData.protocol == 38){
            super.handle(() -> countDownTimer.start(bacData.data.timeMillisecond, i->{
                if(bridge != null) bridge.betCountdown(i);
            }));
        }

    }
}
