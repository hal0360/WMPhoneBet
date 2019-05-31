package tw.com.lixin.wmphonebet.jsonData;

import java.util.Map;

public class Server31 {

    public Data data;

    // public int protocol;

    public interface CmdData {
        void exec(Data data);
    }
    public class Data{
        public int gameID;
        public int groupID;
        public int memberID;
        public float moneyWinLoss;
        public float moneyWin;
        public Map<Integer, String> dtMoneyWin;
    }

}
