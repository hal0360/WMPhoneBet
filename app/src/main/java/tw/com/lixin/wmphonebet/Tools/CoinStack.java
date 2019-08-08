package tw.com.lixin.wmphonebet.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.jsonData.Client22;
import tw.com.lixin.wmphonebet.models.CoinHolder;
import tw.com.lixin.wmphonebet.models.CoinStackData;

@SuppressLint("SetTextI18n")
public class CoinStack extends ConstraintLayout implements Animation.AnimationListener{

    private ImageView coin1, coin2, coin3, coin4;
    private Animation animeDwn, animeUp;
    private int hit = 0;
    private List<Integer> ids = new ArrayList<>();
    private TextView valTxt;
    public CoinStackData data;
    private Boolean disabled = true;

    public void disable(Boolean disabled){
        this.disabled = disabled;
    }

    public CoinStack(Context context) {
        super(context);
        init(context);
    }

    public CoinStack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void cancelBet(){
        data.cancelBet();
        refresh();
    }

    public void repeatBet(){
        data.repeatBet();
        refresh();
    }

    public void clearCoin(){
        data.clear();
        reset();
    }

    private void init(Context context) {
        View.inflate(context, R.layout.coin_stack_layout, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        this.setClipChildren(false);
        this.setClipToPadding(false);
        coin1 = findViewById(R.id.coin1);
        coin2 = findViewById(R.id.coin2);
        coin3 = findViewById(R.id.coin3);
        coin4 = findViewById(R.id.coin4);
        valTxt = findViewById(R.id.stack_value);
        coin1.setVisibility(View.INVISIBLE);
        coin2.setVisibility(View.INVISIBLE);
        coin3.setVisibility(View.INVISIBLE);
        coin4.setVisibility(View.INVISIBLE);
        valTxt.setVisibility(View.INVISIBLE);
        animeDwn = AnimationUtils.loadAnimation(context, R.anim.coin_anime_down);
        animeDwn.setAnimationListener(this);
        animeUp = AnimationUtils.loadAnimation(context, R.anim.coin_anime_up);
    }

    public void setUp(CoinStackData cData){
        data = cData;
        for(CoinHolder coin: data.addedCoin) noAnimeAdd(coin);
        for(CoinHolder coin: data.tempAddedCoin) noAnimeAdd(coin);
    }

    public void refresh(){
        reset();
        for(CoinHolder coin: data.addedCoin) noAnimeAdd(coin);
        for(CoinHolder coin: data.tempAddedCoin) noAnimeAdd(coin);
    }

    private void reset(){
        hit = 0;
        valTxt.setText(data.value + "");
        coin1.setVisibility(View.INVISIBLE);
        coin2.setVisibility(View.INVISIBLE);
        coin3.setVisibility(View.INVISIBLE);
        coin4.setVisibility(View.INVISIBLE);
        valTxt.setVisibility(View.INVISIBLE);
        ids = new ArrayList<>();
    }

    private void noAnimeAdd(CoinHolder coin){
        ids.add(coin.img_res);
        if(hit == 0){
            coin1 .setImageResource(coin.img_res);
            coin1.setVisibility(View.VISIBLE);
        }else if(hit == 1){
            coin2.setImageResource(coin.img_res);
            coin2.setVisibility(View.VISIBLE);
        }else if( hit == 2){
            coin3.setImageResource(coin.img_res);
            coin3.setVisibility(View.VISIBLE);
        }else if(hit == 3){
            coin4.setImageResource(coin.img_res);
            coin4.setVisibility(View.VISIBLE);
        }else{
            ids.remove(0);
            coin4.setImageResource(coin.img_res);
            coin1.setImageResource(ids.get(0));
            coin2.setImageResource(ids.get(1));
            coin3.setImageResource(ids.get(2));
        }
        hit++;
        valTxt.setVisibility(View.VISIBLE);
        valTxt.setText(data.value + "");
    }

    public void add(CoinHolder coin){
        if(disabled) return;
        if(!data.add(coin)) return;
        valTxt.setVisibility(View.VISIBLE);
        valTxt.setText(data.value + "");
        ids.add(coin.img_res);
        if(hit == 0){
            coin1.setImageResource(coin.img_res);
            coin1.setVisibility(View.VISIBLE);
            coin1.startAnimation(animeUp);
        }else if(hit == 1){
            coin2.setImageResource(coin.img_res);
            coin2.setVisibility(View.VISIBLE);
            coin2.startAnimation(animeUp);
        }else if( hit == 2){
            coin3.setImageResource(coin.img_res);
            coin3.setVisibility(View.VISIBLE);
            coin3.startAnimation(animeUp);
        }else if(hit == 3){
            coin4.setImageResource(coin.img_res);
            coin4.setVisibility(View.VISIBLE);
            coin4.startAnimation(animeUp);
        }else{
            ids.remove(0);
            coin4.setImageResource(coin.img_res);
            coin4.startAnimation(animeUp);
            coin1.startAnimation(animeDwn);
        }
        hit++;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        coin1.setImageResource(ids.get(0));
        coin2.setImageResource(ids.get(1));
        coin3.setImageResource(ids.get(2));
    }
    @Override
    public void onAnimationRepeat(Animation animation) {}
    @Override
    public void onAnimationStart(Animation animation) {}
}
