package tw.com.lixin.wmphonebet.jsonData;

public class Server25 {
    public Data data;
   // public int protocol;

    public interface CmdData {
        void exec(Data data);
    }

    public class Data{
        public int gameID;
        public int groupID;
        public int bankerScore;
        public int playerScore;
        public int result;
    }
}
