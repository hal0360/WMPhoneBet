package tw.com.lixin.wmphonebet.jsonData;

import java.util.Map;

public class Server10 {
  //  public int protocol;
    public Data data;

    public interface CmdData {
        void exec(Data data);
    }


    public class Data{
        public Map<Integer, String> dtOdds;
        public int gameID;
        public int groupID;
        public Boolean bOk;
        public int areaID;
        public int areaType;
        public float balance;
        public int maxBet01;
        public int maxBet02;
        public int maxBet03;
        public int maxBet04;
        public int minBet01;
        public int minBet02;
        public int minBet03;
        public int minBet04;
    }


}
