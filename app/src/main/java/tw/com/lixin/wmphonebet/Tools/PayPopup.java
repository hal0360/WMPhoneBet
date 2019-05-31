package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import tw.com.atromoby.utils.Kit;
import tw.com.atromoby.widgets.Popup;
import tw.com.atromoby.widgets.SpinList;
import tw.com.lixin.wmcasino.R;

public class PayPopup extends Popup {

    public PayPopup(Context context) {
        super(context, R.layout.pay_popup);

        SpinList spinList = findViewById(R.id.pay_channel);
        spinList.init(new String[]{"channel1","channel2","channel3"});
        spinList.setColor("#000000");

        SpinList spinList2 = findViewById(R.id.pay_bank);
        spinList2.init(new String[]{"bank1","bank2","bank3"});
        spinList2.setColor("#000000");

        ScrollView scrollViewPop = findViewById(R.id.scroll_view_pop);
        scrollViewPop.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollX = scrollViewPop.getScrollY();
                Kit.alert(context,"okokok " + scrollX);
            }
        });

        clicked(R.id.close_btn,v -> dismiss());
    }


}
