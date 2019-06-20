package tw.com.lixin.wmphonebet.jsonData;

import java.util.List;
import java.util.Map;

public class BacData  {
    public int protocol;
    public Data data;

    public class Data {
        public int gameID;
        public int groupID;
        public Boolean bOk;
        public int areaID;
        public int areaType;
        public float balance;
        public int gameStage;
        public int cardArea;
        public int cardID;
        public int bankerScore;
        public int playerScore;
        public int result;
        public List<Integer> historyArr;
        public HisData historyData;
        public int groupType;
        public int timeMillisecond;
        public Map<Integer, String> dtOdds;
        public int maxBet01;
        public int maxBet02;
        public int maxBet03;
        public int maxBet04;
        public int minBet01;
        public int minBet02;
        public int minBet03;
        public int minBet04;
        public float moneyWinLoss;
        public float moneyWin;
        public int memberID;
        public Map<Integer, String> dtMoneyWin;
    }

    public class HisData{
        public int bankerCount;
        public int playerCount;
        public int bankerPairCount;
        public int playerPairCount;
        public int tieCount;
    }
}
