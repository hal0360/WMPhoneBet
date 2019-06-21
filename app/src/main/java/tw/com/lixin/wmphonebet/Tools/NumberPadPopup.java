package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;
import android.widget.TextView;

import tw.com.atromoby.widgets.Popup;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.models.CostomCoinHolder;


public class NumberPadPopup extends Popup {

    private String numStr = "";

    public NumberPadPopup(Context context, CostomCoinHolder customHolder) {
        super(context, R.layout.number_input_popup);

        TextView display = findViewById(R.id.display_txtgg);


        if(customHolder.value != 0){

            display.setText(customHolder.value + "");
        }

        clicked(R.id.num_0,v->{
            numStr = numStr + "0";
            display.setText(numStr);
        });

        clicked(R.id.num_1,v->{
            numStr = numStr + "1";
            display.setText(numStr);
        });

        clicked(R.id.num_2,v->{
            numStr = numStr + "2";
            display.setText(numStr);
        });

        clicked(R.id.num_3,v->{
            numStr = numStr + "3";
            display.setText(numStr);
        });

        clicked(R.id.num_4,v->{
            numStr = numStr + "4";
            display.setText(numStr);
        });

        clicked(R.id.num_5,v->{
            numStr = numStr + "5";
            display.setText(numStr);
        });

        clicked(R.id.num_6,v->{
            numStr = numStr + "6";
            display.setText(numStr);
        });

        clicked(R.id.num_7,v->{
            numStr = numStr + "7";
            display.setText(numStr);
        });

        clicked(R.id.num_8,v->{
            numStr = numStr + "8";
            display.setText(numStr);
        });

        clicked(R.id.num_9,v->{
            numStr = numStr + "9";
            display.setText(numStr);
        });

        clicked(R.id.confirm_btn,v->{

            if(numStr.equals("")) numStr = "0";


            int setVal = Integer.parseInt(numStr);

            if(setVal == 0){
                customHolder.display.setText(10+"");
                customHolder.setVal(10);
            }else{
                customHolder.display.setText(numStr);
                customHolder.setVal(setVal);
            }

            dismiss();

        });


        clicked(R.id.clear_btn,v->{
            numStr = "";
            display.setText("0");
        });
    }
}
