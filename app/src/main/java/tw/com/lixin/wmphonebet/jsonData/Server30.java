package tw.com.lixin.wmphonebet.jsonData;

public class Server30 {
    public Data data;

    // public int protocol;

    public interface CmdData {
        void exec(Data data);
    }

    public class Data{
        public float balance;
        public int memberID;
    }
}


