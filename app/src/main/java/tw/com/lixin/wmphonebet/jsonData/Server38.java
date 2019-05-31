package tw.com.lixin.wmphonebet.jsonData;

public class Server38 {

    public interface CmdData {
        void exec(Data data);
    }
    public Data data;

    public class Data{
        public int gameID;
        public int groupID;
        public int timeMillisecond;
    }


}
