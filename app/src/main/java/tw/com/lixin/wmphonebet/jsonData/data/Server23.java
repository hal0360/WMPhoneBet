package tw.com.lixin.wmphonebet.jsonData.data;

public class Server23 {

    public Data data;

    public interface CmdData {
        void exec(Data data);
    }


    public class Data{
        public int gameID;
        public int groupID;
        public int groupType;
        public int memberID;
        public float balance;
    }
}
