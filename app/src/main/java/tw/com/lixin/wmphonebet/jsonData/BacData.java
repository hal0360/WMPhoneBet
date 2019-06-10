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
    }

    public class HisData{
        public int bankerCount;
        public int playerCount;
        public int bankerPairCount;
        public int playerPairCount;
        public int tieCount;
    }
}
