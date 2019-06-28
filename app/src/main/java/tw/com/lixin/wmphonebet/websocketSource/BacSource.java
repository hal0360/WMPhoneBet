package tw.com.lixin.wmphonebet.websocketSource;

import android.content.Context;
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
        defineURL(Url.Bac);
    }

    private BacBridge bridge;
    public boolean comission = false;
    public boolean cardIsOpening = true;
    public boolean isBettingNow = true;
    public int groupID = -1;
    public int gameID = 109;
    public int areaID;
    public CountDown countDownTimer;
    public CoinStackData stackLeft, stackRight, stackBTL, stackBTR, stackTop, stackSuper;
    public SparseIntArray pokers;
    public int cardStatus = 0;
    public boolean displayCard = false;

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
    private Popup winPopup;

    public void bind(BacBridge bridge){
        this.bridge = bridge;
    }

    public void unbind(){
        this.bridge = null;
    }

    public void handle(Cmd cmd){
        if(bridge == null) return;
        super.handlePost(cmd);
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
        BacData bacData = Json.from(text, BacData.class);
        if(bacData.data.gameID != gameID || bacData.data.groupID != groupID) return;
        if(bacData.protocol == 10){
            if (bacData.data.bOk) {
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
            if(cOk != null) handlePost(()-> {
                cOk.exec(bacData.data.bOk);
                cOk = null;
            });
        }else if(bacData.protocol == 20){
            if (winPopup != null) winPopup.dismiss();
            isBettingNow = false;
            cardIsOpening = false;
            displayCard = false;
            if (bacData.data.gameStage == 1) {
                pokers = new SparseIntArray();
                isBettingNow = true;
                pokerWin = -1;
            } else if (bacData.data.gameStage == 2) {
                cardIsOpening = true;
                countDownTimer.cancel();
                displayCard = true;
            }
            cardStatus = bacData.data.gameStage;
            handle(() -> bridge.statusUpdate());
        }else if(bacData.protocol == 22){
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
            Context context = (Context) bridge;
            winPopup = new Popup(context, R.layout.win_loss_popup);
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
            if (bacData.data.dtMoneyWin.get(2) == null) {
                mText.setText("");
            } else {
                mText.setText(bacData.data.dtMoneyWin.get(2) + "");
            }
            mText = winPopup.findViewById(R.id.banker_win);
            if (bacData.data.dtMoneyWin.get(1) == null) {
                mText.setText("");
            } else {
                mText.setText(bacData.data.dtMoneyWin.get(1) + "");
            }
            mText = winPopup.findViewById(R.id.player_pair_win);
            if (bacData.data.dtMoneyWin.get(5) == null) {
                mText.setText("");
            } else {
                mText.setText(bacData.data.dtMoneyWin.get(5) + "");
            }
            mText = winPopup.findViewById(R.id.banker_pair_win);
            if (bacData.data.dtMoneyWin.get(4) == null) {
                mText.setText("");
            } else {
                mText.setText(bacData.data.dtMoneyWin.get(4) + "");
            }
            mText = winPopup.findViewById(R.id.tie_win);
            if (bacData.data.dtMoneyWin.get(3) == null) {
                mText.setText("");
            } else {
                mText.setText(bacData.data.dtMoneyWin.get(3) + "");
            }
            mText = winPopup.findViewById(R.id.super_win);
            if (bacData.data.dtMoneyWin.get(8) == null) {
                mText.setText("");
            } else {
                mText.setText(bacData.data.dtMoneyWin.get(8) + "");
            }
            mText = winPopup.findViewById(R.id.total_win_money);
            mText.setText(bacData.data.moneyWin + "");
            winPopup.show();
        }else if(bacData.protocol == 38){
            handle(() -> countDownTimer.start(bacData.data.timeMillisecond, i->{
                if(!cardIsOpening){
                    if(bridge != null) bridge.betCountdown(i);
                }
            }));
        }

    }
}
