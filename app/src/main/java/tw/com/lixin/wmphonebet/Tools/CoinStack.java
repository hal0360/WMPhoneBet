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

import tw.com.atromoby.utils.Kit;
import tw.com.lixin.wmcasino.CasinoActivity;
import tw.com.lixin.wmcasino.CoinHolder;
import tw.com.lixin.wmcasino.R;
import tw.com.lixin.wmcasino.jsonData.Client22;

@SuppressLint("SetTextI18n")
public class CoinStack extends ConstraintLayout implements Animation.AnimationListener{

    private ImageView coin1, coin2, coin3, coin4;
    private Animation animeDwn, animeUp;
    private int hit = 0;
    private List<Integer> ids = new ArrayList<>();
    private TextView valTxt;
    public int value = 0;
    public int maxValue = 999;
    private CasinoActivity context;

    public List<CoinHolder> addedCoin;
    public List<CoinHolder> tempAddedCoin;

    public CoinStackBack back;

    public CoinStack(Context context) {
        super(context);
        init(context);
    }

    public CoinStack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = (CasinoActivity) context;

        View.inflate(context, R.layout.coin_stack_layout, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        this.setClipChildren(false);
        this.setClipToPadding(false);
        coin1 = findViewById(R.id.coin1);
        coin2 = findViewById(R.id.coin2);
        coin3 = findViewById(R.id.coin3);
        coin4 = findViewById(R.id.coin4);
        valTxt = findViewById(R.id.stack_value);
        valTxt.setText(value + "");
        coin1.setVisibility(View.INVISIBLE);
        coin2.setVisibility(View.INVISIBLE);
        coin3.setVisibility(View.INVISIBLE);
        coin4.setVisibility(View.INVISIBLE);
        valTxt.setVisibility(View.INVISIBLE);

        animeDwn = AnimationUtils.loadAnimation(context, R.anim.coin_anime_down);
        animeDwn.setAnimationListener(this);
        animeUp = AnimationUtils.loadAnimation(context, R.anim.coin_anime_up);
    }

    public void resetFromBack(CoinStackBack cback){
        back = cback;
        addedCoin = back.addedCoin;
        tempAddedCoin = back.tempAddedCoin;
        for(CoinHolder coin: addedCoin){
            addedAdd(coin);
        }
        for(CoinHolder coin: tempAddedCoin){
            addedAdd(coin);
        }
    }

    private void addToBack(){
        back.addedCoin = addedCoin;
        back.tempAddedCoin = tempAddedCoin;
    }

    private void reset(){
        value = 0;
        hit = 0;
        valTxt.setText(value + "");
        coin1.setVisibility(View.INVISIBLE);
        coin2.setVisibility(View.INVISIBLE);
        coin3.setVisibility(View.INVISIBLE);
        coin4.setVisibility(View.INVISIBLE);
        valTxt.setVisibility(View.INVISIBLE);
        ids = new ArrayList<>();
    }

    public void clearCoin(){
        reset();
        addedCoin = new ArrayList<>();
        tempAddedCoin = new ArrayList<>();
        addToBack();

    }

    public void cancelBet(){
        reset();
        tempAddedCoin = new ArrayList<>();
        for(CoinHolder coin: addedCoin){
            addedAdd(coin);
        }
        addToBack();
    }

    public void repeatBet(){
        List<CoinHolder> repeatCoin = new ArrayList<>();
        for(CoinHolder coin: tempAddedCoin){
            addedAdd(coin);
            repeatCoin.add(coin);
        }
        tempAddedCoin.addAll(repeatCoin);
        addToBack();
    }

    private void addedAdd(CoinHolder coin){
        value = value + coin.value;
        if(value > maxValue){
            value = value - coin.value;
            Kit.alert(context, "Exceeded max value!");
            return;
        }

        ids.add(coin.img_res);
        if(hit == 0){
            coin4.setImageResource(coin.img_res);
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
        valTxt.setText(value + "");
    }

    public void addCoinToClient(Client22 client22, int area){
        for(CoinHolder coin: tempAddedCoin){
            client22.addBet(area,coin.value);
        }
    }

    public void comfirmBet(){
        addedCoin.addAll(tempAddedCoin);
        tempAddedCoin = new ArrayList<>();
        addToBack();
    }

    public boolean isEmpty(){
        return tempAddedCoin.size() == 0;
    }

    public boolean add(CoinHolder coin){

        if(context.confirmBtn.isDisabled()){
           Kit.alert(context, "Please wait!");
            return false;
        }

        value = value + coin.value;
        if(value > maxValue){
            value = value - coin.value;
            Kit.alert(context, "Exceeded max value!");
            return false;
        }

        tempAddedCoin.add(coin);

        valTxt.setVisibility(View.VISIBLE);
        valTxt.setText(value + "");
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
        addToBack();
        return true;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        coin1.setImageResource(ids.get(0));
        coin2.setImageResource(ids.get(1));
        coin3.setImageResource(ids.get(2));
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
