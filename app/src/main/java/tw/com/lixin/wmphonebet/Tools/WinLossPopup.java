package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;
import android.widget.TextView;

import tw.com.atromoby.widgets.Popup;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.jsonData.BacData;
import tw.com.lixin.wmphonebet.websocketSource.BacSource;

public class WinLossPopup extends Popup {

    TextView player_bet, banker_bet, player_pair_bet, banker_pair_bet, tie_bet, super_bet, player_win, banker_win, player_pair_win, banker_pair_win, tie_win, super_win, total_win_money;

    public WinLossPopup(Context context) {
        super(context, R.layout.win_loss_popup);

        player_bet = findViewById(R.id.player_bet);
        banker_bet = findViewById(R.id.banker_bet);
        player_pair_bet = findViewById(R.id.player_pair_bet);
        banker_pair_bet = findViewById(R.id.banker_pair_bet);
        tie_bet = findViewById(R.id.tie_bet);
        super_bet = findViewById(R.id.super_bet);
        player_win = findViewById(R.id.player_win);
        banker_win = findViewById(R.id.banker_win);
        player_pair_win = findViewById(R.id.player_pair_win);
        banker_pair_win = findViewById(R.id.banker_pair_win);
        tie_win = findViewById(R.id.tie_win);
        super_win = findViewById(R.id.super_win);
        total_win_money = findViewById(R.id.total_win_money);
    }

    public void setUp(BacSource source, BacData bacData){

        player_bet.setText(source.stackLeft.value + "");
        banker_bet.setText(source.stackRight.value + "");
        player_pair_bet.setText(source.stackBTL.value + "");
        banker_pair_bet.setText(source.stackBTR.value + "");
        tie_bet.setText(source.stackTop.value + "");
        super_bet.setText(source.stackSuper.value + "");

        if (bacData.data.dtMoneyWin.get(2) == null) {
            player_win.setText("");
        } else {
            player_win.setText(bacData.data.dtMoneyWin.get(2) + "");
        }
        if (bacData.data.dtMoneyWin.get(1) == null) {
            banker_win.setText("");
        } else {
            banker_win.setText(bacData.data.dtMoneyWin.get(1) + "");
        }
        if (bacData.data.dtMoneyWin.get(5) == null) {
            player_pair_win.setText("");
        } else {
            player_pair_win.setText(bacData.data.dtMoneyWin.get(5) + "");
        }
        if (bacData.data.dtMoneyWin.get(4) == null) {
            banker_pair_win.setText("");
        } else {
            banker_pair_win.setText(bacData.data.dtMoneyWin.get(4) + "");
        }
        if (bacData.data.dtMoneyWin.get(3) == null) {
            tie_win.setText("");
        } else {
            tie_win.setText(bacData.data.dtMoneyWin.get(3) + "");
        }
        if (bacData.data.dtMoneyWin.get(8) == null) {
            super_win.setText("");
        } else {
            super_win.setText(bacData.data.dtMoneyWin.get(8) + "");
        }
        total_win_money.setText(bacData.data.moneyWin + "");
    }
}
