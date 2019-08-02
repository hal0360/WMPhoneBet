package tw.com.lixin.wmphonebet.models;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tw.com.lixin.wmphonebet.jsonData.Client22;
import tw.com.lixin.wmphonebet.models.CoinHolder;

public class CoinStackData {
    public int value = 0;
    public int maxValue = 999;

    public List<CoinHolder> addedCoin = new ArrayList<>();
    public List<CoinHolder> tempAddedCoin = new ArrayList<>();

    public void clear(){
        value = 0;
        addedCoin = new ArrayList<>();
        tempAddedCoin = new ArrayList<>();
    }

    public void addCoinToClient(Client22 client22, int area){
        for(CoinHolder coin: tempAddedCoin){
            client22.addBet(area,coin.value);
        }
    }

    public void comfirmBet(){
        addedCoin.addAll(tempAddedCoin);
        tempAddedCoin = new ArrayList<>();
    }

    public boolean isEmpty(){
        return tempAddedCoin.size() == 0;
    }

    public boolean add(CoinHolder coin){
        value = value + coin.value;
        if(value > maxValue){
            value = value - coin.value;
            return false;
        }
        tempAddedCoin.add(coin);
        return true;
    }

    public void cancelBet(){
        value = 0;
        tempAddedCoin = new ArrayList<>();
        for(CoinHolder coin: addedCoin) add(coin);
    }

    public void repeatBet(){
        List<CoinHolder> repeatCoin = new ArrayList<>(tempAddedCoin);
        for(CoinHolder coin: repeatCoin){
            add(coin);
        }
    }
}
