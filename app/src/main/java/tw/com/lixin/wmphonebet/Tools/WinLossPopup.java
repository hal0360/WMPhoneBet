package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;
import android.widget.TextView;

import tw.com.atromoby.widgets.Popup;
import tw.com.lixin.wmcasino.R;
import tw.com.lixin.wmcasino.jsonData.Server31;
import tw.com.lixin.wmphonebet.jsonData.BacData;

public class WinLossPopup extends Popup {

    TextView playerBet, bankerBet, playerPairBet, bankePairBet, tieBet, superBet, playerWin, bankerWin, playerPairWin, bankePairWin, tieWin, superWin, totalWinMoney;

    public WinLossPopup(Context context) {
        super(context, R.layout.win_loss_popup);

        TextView playerBet = findViewById(R.id.player_bet);
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

    }

    public void init(BacData data){

    }
}

