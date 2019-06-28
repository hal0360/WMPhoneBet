package tw.com.lixin.wmphonebet.models;

import android.view.View;

import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmphonebet.BacActivity;
import tw.com.lixin.wmphonebet.R;

public class CoinHolder extends ItemHolder {

    public int value, img_res;
    public boolean selected = false;

    public CoinHolder(int img_id, int value) {
        super(R.layout.coin_item);
        this.value = value;
        img_res = img_id;
    }

    @Override
    public void onBind() {

        View coin = findViewById(R.id.coin);
        coin.setBackgroundResource(img_res);
        View chip = findViewById(R.id.chip);

        clicked(R.id.coin, v -> {
            if(!selected){
                selected = true;
                BacActivity act = (BacActivity) getContex();
                act.curCoin.selected = false;
                act.curCoin = this;
                act.coinsView.refresh();

            }
        });

        if(selected){
            chip.setBackgroundResource(R.drawable.outer_glow);
        }else {
            chip.setBackgroundResource(0);
        }

    }

    @Override
    public void onRecycle() {

    }




}
