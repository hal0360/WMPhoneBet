package tw.com.lixin.wmphonebet.jsonData;

import java.util.ArrayList;
import java.util.List;

public class Client22 {

    public Data data = new Data();
    int protocol = 22;

    public Client22(){
    }

    public class Data{
        public List<BetStack> betArr = new ArrayList<>();
        public int commission = 0;
    }

    public void addBet(int area, int val){
        BetStack betStack = new BetStack();
        betStack.addBetMoney = val;
        betStack.betArea = area + "";
        data.betArr.add(betStack);
    }

    public class BetStack{
        public String betArea;
        public int addBetMoney;

    }

}
