package tw.com.lixin.wmphonebet.jsonData;

import java.util.List;

public class Server26 {
    public Data data;
  //  public int protocol;

    public interface CmdData {
        void exec(Data data);
    }


    public class Data{
        public int gameID;
        public int groupID;
        public List<Integer> historyArr;
        public HisData historyData;
        public int groupType;

    }

    public class HisData{
        public int bankerCount;
        public int playerCount;
        public int bankerPairCount;
        public int playerPairCount;
        public int tieCount;
    }
}