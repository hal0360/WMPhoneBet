package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;

import tw.com.atromoby.widgets.Popup;
import tw.com.lixin.wmcasino.R;
import tw.com.lixin.wmcasino.jsonData.Server31;

public class WinLossPopup extends Popup {

    public WinLossPopup(Context context) {
        super(context, R.layout.win_loss_popup);
    }

    public void result(Server31.Data data){

    }
}

