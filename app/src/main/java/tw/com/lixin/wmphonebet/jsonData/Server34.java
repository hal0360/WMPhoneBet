package tw.com.lixin.wmphonebet.jsonData;

public class Server34 {

    public Data data;

    // public int protocol;

    public interface CmdData {
        void exec(Data data);
    }

    public class Data{
        public int gameID;
        public int onlinePeople;
    }
}
